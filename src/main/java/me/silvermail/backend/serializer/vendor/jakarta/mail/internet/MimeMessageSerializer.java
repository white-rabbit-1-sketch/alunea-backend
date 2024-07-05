package me.silvermail.backend.serializer.vendor.jakarta.mail.internet;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.jackson.JsonComponent;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@JsonComponent
public class MimeMessageSerializer extends JsonSerializer<MimeMessage> {

    @Override
    public void serialize(MimeMessage mimeMessage, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            mimeMessage.writeTo(outputStream);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        String mimeMessageContent = Base64.getEncoder().encodeToString(outputStream.toString().getBytes());

        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("content", mimeMessageContent);
        jsonGenerator.writeEndObject();
    }
}