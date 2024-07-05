package me.silvermail.backend.service.email.external.handler.processor.outbound;

import me.silvermail.backend.entity.alias.ContactAlias;
import me.silvermail.backend.model.email.external.ExternalEmail;

abstract public class AbstractOutboundProcessor {
    abstract public void process(ExternalEmail externalEmail, ContactAlias recipientContactAlias) throws Exception;
}
