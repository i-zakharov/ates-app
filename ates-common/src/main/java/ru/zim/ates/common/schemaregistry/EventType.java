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

    ATES_USER_ROLE_CHANGED (MqConfig.ATES_USERS_EXCHANGE, EventCategory.BUSINESS),

    ATES_TASK_CREATED(MqConfig.ATES_TASKS_STREAM_EXCHANGE, EventCategory.CUD),
    ATES_TASK_UPDATED(MqConfig.ATES_TASKS_STREAM_EXCHANGE, EventCategory.CUD),

    ATES_TASK_PENDING(MqConfig.ATES_TASKS_EXCHANGE, EventCategory.BUSINESS), //Task создан и ожидает назначения цены для переход в работу
    ATES_TASK_ASSIGNED(MqConfig.ATES_TASKS_EXCHANGE, EventCategory.BUSINESS),
    ATES_TASK_CLOSED(MqConfig.ATES_TASKS_EXCHANGE, EventCategory.BUSINESS),

    ATES_TASK_PRICE_SET(MqConfig.ATES_TASKS_PRICES_EXCHANGE, EventCategory.BUSINESS);

    final String exchangeName;
    final EventCategory eventCategory;

    EventType(String exchangeName,  EventCategory eventCategory) {
        this.exchangeName = exchangeName;
        this.eventCategory = eventCategory;
    }


}
