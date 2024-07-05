package me.silvermail.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.silvermail.backend.entity.alias.MailboxAlias;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class MailboxAliasSerializer extends JsonSerializer<MailboxAlias> {

    @Override
    public void serialize(MailboxAlias mailboxAlias, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", mailboxAlias.getId());
        jsonGenerator.writeStringField("type", mailboxAlias.getType());
        jsonGenerator.writeObjectField("mailbox", mailboxAlias.getMailbox());
        jsonGenerator.writeObjectField("domain", mailboxAlias.getDomain());
        jsonGenerator.writeStringField("recipient", mailboxAlias.getRecipient());
        jsonGenerator.writeBooleanField("isEnabled", mailboxAlias.isEnabled());
        jsonGenerator.writeEndObject();
    }
}