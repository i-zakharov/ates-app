package ru.zim.ates.common.schemaregistry;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum EventType {
    ATES_TEST (MqConfig.ATES_TEST_EXCHANGE, EventCategory.BUSINESS),

    ATES_USER_CREATED (MqConfig.ATES_USERS_STREAM_EXCHANGE, EventCategory.CUD),
    ATES_USER_UPDATED (MqConfig.ATES_USERS_STREAM_EXCHANGE, EventCategory.CUD),

    ATES_USER_ROLE_CHANGED (MqConfig.ATES_USERS_EXCHANGE, EventCategory.BUSINESS);
    //ATES_USER_BLOCKED (MqConfig.ATES_USERS_EXCHANGE, EventCategory.BUSINESS),
    //ATES_USER_UNBLOCKED (MqConfig.ATES_USERS_EXCHANGE, EventCategory.BUSINESS);

    final String exchangeName;
    final EventCategory eventCategory;

    EventType(String exchangeName,  EventCategory eventCategory) {
        this.exchangeName = exchangeName;
        this.eventCategory = eventCategory;
    }


}
