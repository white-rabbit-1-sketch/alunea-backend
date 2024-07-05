package me.silvermail.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.silvermail.backend.entity.alias.ContactAlias;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ContactAliasSerializer extends JsonSerializer<ContactAlias> {

    @Override
    public void serialize(ContactAlias contactAlias, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", contactAlias.getId());
        jsonGenerator.writeStringField("type", contactAlias.getType());
        jsonGenerator.writeObjectField("contact", contactAlias.getContact());
        jsonGenerator.writeObjectField("mailboxAlias", contactAlias.getMailboxAlias());
        jsonGenerator.writeObjectField("domain", contactAlias.getDomain());
        jsonGenerator.writeStringField("recipient", contactAlias.getRecipient());
        jsonGenerator.writeBooleanField("isEnabled", contactAlias.isEnabled());
        jsonGenerator.writeEndObject();
    }
}