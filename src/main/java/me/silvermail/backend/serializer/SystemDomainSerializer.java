package me.silvermail.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.silvermail.backend.entity.domain.SystemDomain;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class SystemDomainSerializer extends JsonSerializer<SystemDomain> {

    @Override
    public void serialize(SystemDomain systemDomain, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", systemDomain.getId());
        jsonGenerator.writeStringField("type", systemDomain.getType());
        jsonGenerator.writeStringField("domain", systemDomain.getDomain());
        jsonGenerator.writeBooleanField("isEnabled", systemDomain.isEnabled());
        jsonGenerator.writeNumberField("aliasesCount", systemDomain.getMailboxAliasList().size());
        jsonGenerator.writeEndObject();
    }
}