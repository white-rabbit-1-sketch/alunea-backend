package me.silvermail.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.silvermail.backend.entity.Contact;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ContactSerializer extends JsonSerializer<Contact> {

    @Override
    public void serialize(Contact contact, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", contact.getId());
        jsonGenerator.writeStringField("email", contact.getEmail());
        jsonGenerator.writeBooleanField("isEnabled", contact.isEnabled());
        jsonGenerator.writeNumberField("aliasesCount", contact.getContactAliasList().size());
        jsonGenerator.writeEndObject();
    }
}