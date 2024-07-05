package me.silvermail.backend.service.email.external.handler;

import lombok.extern.slf4j.Slf4j;
import me.silvermail.backend.entity.User;
import me.silvermail.backend.entity.alias.AbstractAlias;
import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.entity.domain.AbstractUserDomain;
import me.silvermail.backend.exception.email.external.RecipientAliasNotFoundException;
import me.silvermail.backend.model.email.external.ExternalEmail;
import me.silvermail.backend.service.AliasService;
import me.silvermail.backend.service.DomainService;
import me.silvermail.backend.service.ExternalEmailService;
import me.silvermail.backend.service.ThrottlerService;
import me.silvermail.backend.service.email.external.AddressService;
import me.silvermail.backend.service.email.external.handler.guard.GeneralGuard;
import me.silvermail.backend.service.email.external.handler.guard.InboundGuard;
import me.silvermail.backend.service.email.external.handler.guard.OutboundGuard;
import me.silvermail.backend.service.email.external.handler.processor.inbound.AbstractInboundProcessor;
import me.silvermail.backend.service.email.external.handler.processor.outbound.AbstractOutboundProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ExternalEmailHandlerService {
    @Autowired
    protected ExternalEmailService externalEmailService;
    @Autowired
    protected AliasService aliasService;
    @Autowired
    protected GeneralGuard generalGuard;
    @Autowired
    protected OutboundGuard outboundGuard;
    @Autowired
    protected InboundGuard inboundGuard;
    @Autowired
    protected ThrottlerService throttlerService;
    @Autowired
    protected DomainService domainService;
    @Autowired
    protected AddressService addressService;


    @Autowired
    @Qualifier("email.external.processor.outbound")
    protected List<AbstractOutboundProcessor> outboundProcessorList;

    @Autowired
    @Qualifier("email.external.processor.inbound")
    protected List<AbstractInboundProcessor> inboundProcessorList;

    protected final Map<String, User> userLockMap = new ConcurrentHashMap<>();

    public void handleEmail(ExternalEmail externalEmail) throws Exception {
        log.info(
                "Start handling an email [originalFrom={}, originalTo={}, from={}, to={}, headerFrom={}, headerTo={}]",
                externalEmail.getOriginalSmtpSenderAddress(),
                externalEmail.getOriginalSmtpRecipientAddress(),
                externalEmail.getSmtpSenderAddress(),
                externalEmail.getSmtpRecipientAddress(),
                externalEmail.getMimeMessage().getFrom(),
                externalEmail.getMimeMessage().getAllRecipients()
        );

        try {
            AbstractAlias recipientAlias = aliasService.getAliasByValue(externalEmail.getSmtpRecipientAddress().getAddress());
            AbstractUserDomain recipientDomain = domainService.getUserDomainByDomain(addressService.getAddressDomain(
                    externalEmail.getSmtpRecipientAddress()
            ));

            generalGuard.check(externalEmail, recipientDomain);

            Object lock = userLockMap.computeIfAbsent(recipientDomain.getUser().getId(), id -> recipientDomain.getUser());
            synchronized (lock) {
                if (recipientAlias != null && Objects.equals(recipientAlias.getType(), ContactAlias.TYPE)) {
                    handleOutboundEmail(externalEmail, (ContactAlias) recipientAlias);
                } else  {
                    handleInboundEmail(externalEmail, (MailboxAlias) recipientAlias, recipientDomain);
                }
            }

            externalEmailService.sendEmail(externalEmail);

            log.info(
                    "External email was handled [originalFrom={}, originalTo={}, from={}, to={}, headerFrom={}, headerTo={}]",
                    externalEmail.getOriginalSmtpSenderAddress(),
                    externalEmail.getOriginalSmtpRecipientAddress(),
                    externalEmail.getSmtpSenderAddress(),
                    externalEmail.getSmtpRecipientAddress(),
                    externalEmail.getMimeMessage().getFrom(),
                    externalEmail.getMimeMessage().getAllRecipients()
            );
        } catch (Throwable e) {
            log.warn(
                    "External email was not handled [originalFrom={}, originalTo={}, from={}, to={}, headerFrom={}, headerTo={}], error: {}",
                    externalEmail.getOriginalSmtpSenderAddress(),
                    externalEmail.getOriginalSmtpRecipientAddress(),
                    externalEmail.getSmtpSenderAddress(),
                    externalEmail.getSmtpRecipientAddress(),
                    externalEmail.getMimeMessage().getFrom(),
                    externalEmail.getMimeMessage().getAllRecipients(),
                    e.toString()
            );

            throw e;
        }
    }

    public void handleOutboundEmail(
            ExternalEmail externalEmail,
            ContactAlias recipientContactAlias
    ) throws Exception {
        outboundGuard.check(externalEmail, recipientContactAlias);

        for (AbstractOutboundProcessor outboundProcessor : outboundProcessorList) {
            log.debug("Apply processor {}: [externalEmail={}, recipientContactAlias={}]", outboundProcessor.getClass().getSimpleName(), externalEmail, recipientContactAlias);
            outboundProcessor.process(externalEmail, recipientContactAlias);
        }

        throttlerService.incrementSentOutboundExternalEmail(recipientContactAlias.getUser());
    }

    public void handleInboundEmail(
            ExternalEmail externalEmail,
            MailboxAlias recipientMailboxAlias,
            AbstractUserDomain recipientDomain
    ) throws Exception {
        inboundGuard.check(externalEmail, recipientMailboxAlias, recipientDomain);

        if (recipientMailboxAlias == null) {
            recipientMailboxAlias = aliasService.createMailboxAlias(
                    recipientDomain.getUser(),
                    recipientDomain.getCatchAllMailbox(),
                    recipientDomain,
                    addressService.getAddressRecipient(externalEmail.getSmtpRecipientAddress())
            );
        }

        for (AbstractInboundProcessor inboundProcessor : inboundProcessorList) {
            log.debug("Apply processor {}: [externalEmail={}, recipientMailboxAlias={}]", inboundProcessor.getClass().getSimpleName(), externalEmail, recipientMailboxAlias);
            inboundProcessor.process(externalEmail, recipientMailboxAlias);
        }
    }
}