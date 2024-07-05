package me.silvermail.backend.deserializer.vendor.jakarta.mail.internet;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.jackson.JsonComponent;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

@JsonComponent
public class MimeMessageDeserializer extends JsonDeserializer<MimeMessage> {

    @Override
    public MimeMessage deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String mimeMessageContent = ((JsonNode) jsonParser.getCodec().readTree(jsonParser).get("content")).textValue();

        byte[] decodedMimeMessageContent = Base64.getDecoder().decode(mimeMessageContent);
        mimeMessageContent = new String(decodedMimeMessageContent);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(mimeMessageContent.getBytes());
        Session session = Session.getDefaultInstance(new Properties());

        MimeMessage mimeMessage;
        try {
            mimeMessage = new MimeMessage(session, inputStream);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return mimeMessage;
    }
}
