package me.silvermail.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.silvermail.backend.entity.domain.CustomDomain;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class CustomDomainSerializer extends JsonSerializer<CustomDomain> {

    @Override
    public void serialize(CustomDomain customDomain, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", customDomain.getId());
        jsonGenerator.writeStringField("type", customDomain.getType());
        jsonGenerator.writeStringField("domain", customDomain.getDomain());
        jsonGenerator.writeBooleanField("isEnabled", customDomain.isEnabled());
        jsonGenerator.writeObjectField("catchAllMailbox", customDomain.getCatchAllMailbox());
        jsonGenerator.writeNumberField("aliasesCount", customDomain.getMailboxAliasList().size());
        jsonGenerator.writeEndObject();
    }
}