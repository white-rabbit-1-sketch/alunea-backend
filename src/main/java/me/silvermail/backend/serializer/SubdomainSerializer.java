package me.silvermail.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.silvermail.backend.entity.domain.Subdomain;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class SubdomainSerializer extends JsonSerializer<Subdomain> {

    @Override
    public void serialize(Subdomain subdomain, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", subdomain.getId());
        jsonGenerator.writeStringField("type", subdomain.getType());
        jsonGenerator.writeStringField("domain", subdomain.getDomain());
        jsonGenerator.writeStringField("subdomain", subdomain.getSubdomain());
        jsonGenerator.writeBooleanField("isEnabled", subdomain.isEnabled());
        jsonGenerator.writeObjectField("catchAllMailbox", subdomain.getCatchAllMailbox());
        jsonGenerator.writeNumberField("aliasesCount", subdomain.getMailboxAliasList().size());
        jsonGenerator.writeEndObject();
    }
}