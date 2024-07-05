package me.silvermail.backend.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import me.silvermail.backend.entity.User;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class UserSerializer extends JsonSerializer<User> {

    @Override
    public void serialize(User user, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", user.getId());
        jsonGenerator.writeStringField("email", user.getEmail());
        jsonGenerator.writeBooleanField("isEmailVerified", user.isEmailVerified());
        jsonGenerator.writeObjectField("favoriteDomain", user.getFavoriteDomain());
        jsonGenerator.writeObjectField("favoriteMailbox", user.getFavoriteMailbox());
        jsonGenerator.writeStringField("language", user.getLanguage());
        jsonGenerator.writeEndObject();
    }
}