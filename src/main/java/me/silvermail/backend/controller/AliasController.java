package me.silvermail.backend.controller;

import jakarta.validation.Valid;
import me.silvermail.backend.dto.controller.request.alias.CreateAliasRequestDto;
import me.silvermail.backend.dto.controller.response.BaseResponseDto;
import me.silvermail.backend.dto.controller.response.alias.AliasListResponseDto;
import me.silvermail.backend.dto.controller.response.alias.AliasRecipientAvailabilityResponseDto;
import me.silvermail.backend.dto.controller.response.alias.AliasResponseDto;
import me.silvermail.backend.entity.Contact;
import me.silvermail.backend.entity.Mailbox;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.AbstractAlias;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.entity.domain.AbstractUserDomain;
import me.silvermail.backend.exception.http.BadRequestHttpException;
import me.silvermail.backend.service.AliasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AliasController {
    @Autowired
    protected AliasService aliasService;

    @GetMapping(path="/domain/{domainId}/recipient/{recipient}")
    @PreAuthorize(
            "hasRole('USER') && " +
            "authentication.getPrincipal().getId() == #domain.getUser().getId()"
    )
    public AliasRecipientAvailabilityResponseDto isAliasRecipientAvailable(
            @PathVariable("domainId") AbstractUserDomain domain,
            @PathVariable String recipient
    ) {
        AbstractAlias alias = aliasService.getAliasByDomainIdAndRecipient(domain.getId(), recipient);

        AliasRecipientAvailabilityResponseDto aliasRecipientAvailabilityResponseDto = new AliasRecipientAvailabilityResponseDto();
        aliasRecipientAvailabilityResponseDto.setAvailable(alias == null);

        return aliasRecipientAvailabilityResponseDto;
    }

    @PostMapping("/user/{userId}/mailbox/{mailboxId}/domain/{domainId}/alias")
    @PreAuthorize(
            "hasRole('USER') && " +
            "authentication.getPrincipal().getId() == #user.getId() && " +
            "authentication.getPrincipal().getId() == #mailbox.getUser().getId() && " +
            "authentication.getPrincipal().getId() == #domain.getUser().getId() &&" +
            "#mailbox.isEnabled() == true && #domain.isEnabled() == true"
    )
    public AliasResponseDto createMailboxAlias(
            @PathVariable("userId") User user,
            @PathVariable("mailboxId") Mailbox mailbox,
            @PathVariable("domainId") AbstractUserDomain domain,
            @RequestBody @Valid CreateAliasRequestDto createAliasRequestDto
    ) {
        if (user.getMailboxAliasList().size() >= 30) {
            throw new BadRequestHttpException("You have reached maximum mailbox aliases count");
        }

        MailboxAlias alias = aliasService.createMailboxAlias(
                user,
                mailbox,
                domain,
                createAliasRequestDto.getRecipient()
        );

        AliasResponseDto aliasResponseDto = new AliasResponseDto();
        aliasResponseDto.setAlias(alias);

        return aliasResponseDto;
    }

    @PostMapping("/user/{userId}/contact/{contactId}/mailbox/alias/{mailboxAliasId}/contact/alias")
    @PreAuthorize(
            "hasRole('USER') && " +
            "authentication.getPrincipal().getId() == #user.getId() && " +
            "authentication.getPrincipal().getId() == #contact.getUser().getId() && " +
            "authentication.getPrincipal().getId() == #mailboxAlias.getUser().getId() &&" +
            "#contact.isEnabled() == true && #mailboxAlias.isEnabled() == true && #mailboxAlias.getDomain().isEnabled() == true"
    )
    public AliasResponseDto createContactAlias(
            @PathVariable("userId") User user,
            @PathVariable("contactId") Contact contact,
            @PathVariable("mailboxAliasId") MailboxAlias mailboxAlias,
            @RequestBody @Valid CreateAliasRequestDto createAliasRequestDto
    ) {
        ContactAlias alias = aliasService.createContactAlias(
                user,
                contact,
                mailboxAlias,
                createAliasRequestDto.getRecipient()
        );

        AliasResponseDto aliasResponseDto = new AliasResponseDto();
        aliasResponseDto.setAlias(alias);

        return aliasResponseDto;
    }

    @GetMapping(path="/user/{userId}/mailbox/alias")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public AliasListResponseDto getMailboxAliasListByUserId(
            @PathVariable("userId") User user
    ) {
        List<MailboxAlias> mailboxAliasList = aliasService.getMailboxAliasListByUserId(user.getId());

        AliasListResponseDto aliasListResponseDto = new AliasListResponseDto();
        aliasListResponseDto.setAliasList(mailboxAliasList);

        return aliasListResponseDto;
    }

    @GetMapping(path="/user/{userId}/contact/alias")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #user.getId()")
    public AliasListResponseDto getContactAliasListByUserId(
            @PathVariable("userId") User user
    ) {
        List<ContactAlias> contactAliasList = aliasService.getContactAliasListByUserId(user.getId());

        AliasListResponseDto aliasListResponseDto = new AliasListResponseDto();
        aliasListResponseDto.setAliasList(contactAliasList);

        return aliasListResponseDto;
    }

    @PostMapping("/alias/{aliasId}/enable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #alias.getUser().getId()")
    public BaseResponseDto enableAlias(
            @PathVariable("aliasId") AbstractAlias alias
    ) {
        alias.setEnabled(true);
        aliasService.saveAlias(alias);

        return new BaseResponseDto();
    }

    @PostMapping("/alias/{aliasId}/disable")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #alias.getUser().getId()")
    public BaseResponseDto disableAlias(
            @PathVariable("aliasId") AbstractAlias alias
    ) {
        alias.setEnabled(false);
        aliasService.saveAlias(alias);

        return new BaseResponseDto();
    }

    @DeleteMapping("/alias/{aliasId}")
    @PreAuthorize("hasRole('USER') && authentication.getPrincipal().getId() == #alias.getUser().getId()")
    public BaseResponseDto removeAlias(
            @PathVariable("aliasId") AbstractAlias alias
    ) {
        aliasService.removeAlias(alias);

        return new BaseResponseDto();
    }
}