package me.silvermail.backend.service;

import me.silvermail.backend.entity.UsedDomain;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.domain.*;
import me.silvermail.backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class DomainService {
    @Autowired
    protected DomainRepository domainRepository;
    @Autowired
    protected CustomDomainRepository customDomainRepository;
    @Autowired
    protected SystemDomainRepository systemDomainRepository;
    @Autowired
    protected SubdomainRepository subdomainRepository;
    @Autowired
    protected UsedDomainRepository usedDomainRepository;

    public AbstractDomain getDomainById(String id) {
        return domainRepository.findById(id).orElse(null);
    }

    public List<CustomDomain> getCustomDomainListByUserId(String userId) {
        return customDomainRepository.findAllByUserId(userId);
    }

    public List<SystemDomain> getSystemDomainList() {
        return Streamable.of(systemDomainRepository.findAll()).toList();
    }

    public List<Subdomain> getSubdomainListByUserId(String userId) {
        return subdomainRepository.findAllByUserId(userId);
    }

    public List<AbstractUserDomain> getUserDomainListByUserId(String userId) {
        List<AbstractUserDomain> userDomainList = new ArrayList<>();
        userDomainList.addAll(getSubdomainListByUserId(userId));
        userDomainList.addAll(getCustomDomainListByUserId(userId));

        return userDomainList;
    }

    public Subdomain getSubdomainBySystemDomainIdAndSubdomain(String systemDomainId, String subdomain) {
        return subdomainRepository.findBySystemDomainIdAndSubdomain(systemDomainId, subdomain);
    }

    public AbstractUserDomain getUserDomainByDomain(String value) {
        AbstractUserDomain result = null;
        AbstractDomain domain = domainRepository.findDomainByDomain(value);

        if (domain instanceof AbstractUserDomain) {
            result = (AbstractUserDomain) domain;
        }

        return result;
    }

    public boolean isUserSubdomainAvailable(SystemDomain systemDomain, String subdomain) {
        Subdomain subdomainInstance = getSubdomainBySystemDomainIdAndSubdomain(
                systemDomain.getId(),
                subdomain
        );

        String domain = buildDomainBySystemDomainAndSubdomain(systemDomain, subdomain);
        UsedDomain usedDomain = usedDomainRepository.findByDomain(domain);

        return subdomainInstance == null && usedDomain == null;
    }

    @Transactional
    public Subdomain createSubdomain(User user, SystemDomain systemDomain, String subdomain) {
        subdomain = subdomain.trim();

        if (subdomain.isEmpty() || !isUserSubdomainAvailable(systemDomain, subdomain)) {
            throw new IllegalArgumentException("Subdomain isn't available");
        }

        Subdomain subdomainInstance = new Subdomain();
        subdomainInstance.setType(Subdomain.TYPE);
        subdomainInstance.setDomain(buildDomainBySystemDomainAndSubdomain(systemDomain, subdomain));
        subdomainInstance.setCreateTime(new Date());
        subdomainInstance.setSubdomain(subdomain);
        subdomainInstance.setUser(user);
        subdomainInstance.setSystemDomain(systemDomain);
        subdomainRepository.save(subdomainInstance);

        UsedDomain usedDomain = new UsedDomain();
        usedDomain.setDomain(subdomainInstance.getDomain());
        usedDomain.setCreateTime(new Date());
        usedDomainRepository.save(usedDomain);

        return subdomainInstance;
    }

    protected String buildDomainBySystemDomainAndSubdomain(SystemDomain systemDomain, String subdomain) {
        return subdomain + "." + systemDomain.getDomain();
    }

    public void saveDomain(AbstractDomain domain) {
        domainRepository.save(domain);
    }

    public void removeDomain(AbstractDomain domain) {
        domainRepository.delete(domain);
    }
}
