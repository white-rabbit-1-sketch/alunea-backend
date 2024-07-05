package me.silvermail.backend.service.email.external.handler.guard;

import lombok.extern.slf4j.Slf4j;
import me.silvermail.backend.entity.domain.AbstractUserDomain;
import me.silvermail.backend.exception.email.external.*;
import me.silvermail.backend.model.email.external.ExternalEmail;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GeneralGuard {
    public void check(
            ExternalEmail externalEmail,
            AbstractUserDomain recipientDomain
    ) {
        if (recipientDomain == null) {
            throw new RecipientAliasNotFoundException();
        }

        if (!recipientDomain.getUser().isEnabled()) {
            throw new UserDisabledException();
        }

        if (!recipientDomain.getUser().isEmailVerified()) {
            throw new UserEmailUnverifiedException();
        }

        if (!recipientDomain.isEnabled()) {
            throw new RecipientAliasDomainDisabledException();
        }
    }
}
