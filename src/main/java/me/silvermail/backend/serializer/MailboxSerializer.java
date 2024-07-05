package me.silvermail.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.silvermail.backend.entity.Mailbox;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class MailboxSerializer extends JsonSerializer<Mailbox> {

    @Override
    public void serialize(Mailbox mailbox, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", mailbox.getId());
        jsonGenerator.writeStringField("email", mailbox.getEmail());
        jsonGenerator.writeBooleanField("isEmailVerified", mailbox.isEmailVerified());
        jsonGenerator.writeBooleanField("isEnabled", mailbox.isEnabled());
        jsonGenerator.writeNumberField("aliasesCount", mailbox.getMailboxAliasList().size());
        jsonGenerator.writeEndObject();
    }
}