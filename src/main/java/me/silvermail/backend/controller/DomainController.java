package me.silvermail.backend.controller;

import jakarta.validation.Valid;
import me.silvermail.backend.dto.controller.request.domain.subdomain.CreateSubdomainRequestDto;
import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.dto.controller.response.domain.DomainAvailabilityResponseDto;
import me.silvermail.backend.dto.controller.response.domain.DomainListResponseDto;
import me.silvermail.backend.dto.controller.response.domain.DomainResponseDto;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.domain.AbstractUserDomain;
import me.silvermail.backend.entity.domain.CustomDomain;
import me.silvermail.backend.entity.domain.Subdomain;
import me.silvermail.backend.entity.domain.SystemDomain;
import me.silvermail.backend.exception.http.BadRequestHttpException;
import me.silvermail.backend.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DomainController {
    @Autowired
    protected DomainService domainService;

    @GetMapping(path="/user/{userId}/domain/custom")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public DomainListResponseDto getCustomDomainListByUserId(
            @PathVariable("userId") User user
    ) {
        List<CustomDomain> customDomainList = domainService.getCustomDomainListByUserId(user.getId());

        DomainListResponseDto domainListResponseDto = new DomainListResponseDto();
        domainListResponseDto.setDomainList(customDomainList);

        return domainListResponseDto;
    }

    @GetMapping(path="/user/{userId}/domain/subdomain")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public DomainListResponseDto getSubdomainListByUserId(
            @PathVariable("userId") User user
    ) {
        List<Subdomain> subdomainList = domainService.getSubdomainListByUserId(user.getId());

        DomainListResponseDto domainListResponseDto = new DomainListResponseDto();
        domainListResponseDto.setDomainList(subdomainList);

        return domainListResponseDto;
    }

    @GetMapping(path="/domain/system")
    @PreAuthorize("hasRole('USER')")
    public DomainListResponseDto getSystemDomainList() {
        List<SystemDomain> systemDomainList = domainService.getSystemDomainList();

        DomainListResponseDto domainListResponseDto = new DomainListResponseDto();
        domainListResponseDto.setDomainList(systemDomainList);

        return domainListResponseDto;
    }

    @GetMapping(path="/domain/system/{systemDomainId}/subdomain/{subdomain}")
    @PreAuthorize("hasRole('USER')")
    public DomainAvailabilityResponseDto isUserSubdomainAvailable(
            @PathVariable("systemDomainId") SystemDomain systemDomain,
            @PathVariable String subdomain
    ) {
        DomainAvailabilityResponseDto domainAvailabilityResponseDto = new DomainAvailabilityResponseDto();
        domainAvailabilityResponseDto.setAvailable(domainService.isUserSubdomainAvailable(systemDomain, subdomain));

        return domainAvailabilityResponseDto;
    }

    @PostMapping("/user/{userId}/domain/system/{systemDomainId}/subdomain")
    @PreAuthorize(
            "hasRole('USER') && authentication.getPrincipal().getId() == #user.getId() && " +
            "#systemDomain.isEnabled() == true"
    )
    public DomainResponseDto createSubdomain(
            @PathVariable("userId") User user,
            @PathVariable("systemDomainId") SystemDomain systemDomain,
            @RequestBody @Valid CreateSubdomainRequestDto createSubdomainRequestDto
    ) {
        if (user.getSubdomainList().size() >= 1) {
            throw new BadRequestHttpException("You have reached maximum subdomains count");
        }

        Subdomain subdomainInstance = domainService.createSubdomain(
                user,
                systemDomain,
                createSubdomainRequestDto.getSubdomain()
        );

        DomainResponseDto domainResponseDto = new DomainResponseDto();
        domainResponseDto.setDomain(subdomainInstance);

        return domainResponseDto;
    }

    @PostMapping("/domain/{domainId}/enable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #domain.getUser().getId()")
    public BaseResponseDto enableDomain(
            @PathVariable("domainId") AbstractUserDomain domain
    ) {
        domain.setEnabled(true);
        domainService.saveDomain(domain);

        return new BaseResponseDto();
    }

    @PostMapping("/domain/{domainId}/disable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #domain.getUser().getId()")
    public BaseResponseDto disableDomain(
            @PathVariable("domainId") AbstractUserDomain domain
    ) {
        domain.setEnabled(false);
        domainService.saveDomain(domain);

        return new BaseResponseDto();
    }

    @DeleteMapping("/domain/{domainId}")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #domain.getUser().getId()")
    public BaseResponseDto removeSubdomain(
            @PathVariable("domainId") AbstractUserDomain domain
    ) {
        domainService.removeDomain(domain);

        return new BaseResponseDto();
    }

    @PostMapping("/domain/{domainId}/mailbox/{mailboxId}/catch-all/enable")
    @PreAuthorize(
            "hasRole('USER') && authentication.getPrincipal().getId() == #domain.getUser().getId() && " +
            "authentication.getPrincipal().getId() == #mailbox.getUser().getId()"
    )
    public BaseResponseDto enableDomainCatchAll(
            @PathVariable("domainId") AbstractUserDomain domain,
            @PathVariable("mailboxId") Mailbox mailbox
    ) {
        domain.setCatchAllMailbox(mailbox);
        domainService.saveDomain(domain);

        return new BaseResponseDto();
    }

    @PostMapping("/domain/{domainId}/catch-all/disable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #domain.getUser().getId()")
    public BaseResponseDto disableDomainCatchAll(
            @PathVariable("domainId") AbstractUserDomain domain
    ) {
        domain.setCatchAllMailbox(null);
        domainService.saveDomain(domain);

        return new BaseResponseDto();
    }
}