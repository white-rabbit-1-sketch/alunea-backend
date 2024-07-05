package me.silvermail.backend.repository;

import me.silvermail.backend.entity.domain.Subdomain;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubdomainRepository extends CrudRepository<Subdomain, String> {
    Optional<Subdomain> findById(String id);
    Subdomain findBySystemDomainIdAndSubdomain(String systemDomainId, String subdomain);
    List<Subdomain> findAllByUserId(String userId);
}