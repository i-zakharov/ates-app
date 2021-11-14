package ru.zim.ates.common.schemaregistry.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Utils {
    public static final ObjectMapper mapper;
    static {
        mapper = new ObjectMapper().findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
