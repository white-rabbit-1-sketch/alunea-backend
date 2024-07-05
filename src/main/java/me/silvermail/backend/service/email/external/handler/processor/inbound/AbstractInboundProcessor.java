package me.silvermail.backend.service.email.external.handler.processor.inbound;

import me.silvermail.backend.entity.alias.MailboxAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;

abstract public class AbstractInboundProcessor {
    abstract public void process(ExternalEmail externalEmail, MailboxAlias recipientMailboxAlias) throws Exception;
}
