package ru.zim.ates.tasktracker.mapper;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;

public class AppUserSerializer extends StdSerializer<AppUser> {
    public AppUserSerializer() {
        this(null);
    }

    public AppUserSerializer(Class<AppUser> t) {
        super(t);
    }

    @Override
    public void serialize(AppUser appUser, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(appUser.getPublicId().toString());
//        jsonGenerator.writeStartObject();
//        jsonGenerator.writeStringField("publicId", appUser.getPublicId().toString());
//        jsonGenerator.writeEndObject();
    }
}
