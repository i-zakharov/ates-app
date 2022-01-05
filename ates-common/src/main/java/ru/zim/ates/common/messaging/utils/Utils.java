package ru.zim.ates.common.messaging.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class Utils {
    public static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper().findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); //Иначе валидатор схемы пдпает с ошибкой: null found, string expected
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String formatExceptionForLogging(Throwable throwable) {
        StringBuilder message = new StringBuilder();
        message.append("Exception message:")
                .append(System.lineSeparator())
                .append(throwable.toString())
                .append(System.lineSeparator())
                .append("Stack trace:")
                .append(System.lineSeparator())
                .append(ExceptionUtils.getStackTrace(throwable));
        return message.toString();

    }
}
