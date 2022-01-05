package ru.zim.ates.common.messaging.config;

import lombok.Getter;
import ru.zim.ates.common.messaging.schemaregistry.EventCategory;
import ru.zim.ates.common.messaging.schemaregistry.EventType;

@Getter
public enum AppEventType implements EventType {
    ATES_TEST(MqConfig.ATES_TEST_EXCHANGE, EventCategory.BUSINESS),

    ATES_USER_CREATED(MqConfig.ATES_USERS_STREAM_EXCHANGE, EventCategory.CUD),
    ATES_USER_UPDATED(MqConfig.ATES_USERS_STREAM_EXCHANGE, EventCategory.CUD),

    ATES_USER_ROLE_CHANGED(MqConfig.ATES_USERS_EXCHANGE, EventCategory.BUSINESS),

    ATES_TASK_CREATED(MqConfig.ATES_TASKS_STREAM_EXCHANGE, EventCategory.CUD),
    ATES_TASK_UPDATED(MqConfig.ATES_TASKS_STREAM_EXCHANGE, EventCategory.CUD),

    ATES_TASK_PENDING(MqConfig.ATES_TASKS_EXCHANGE, EventCategory.BUSINESS), //Task создан и ожидает назначения цены для переход в работу
    ATES_TASK_ASSIGNED(MqConfig.ATES_TASKS_EXCHANGE, EventCategory.BUSINESS),
    ATES_TASK_CLOSED(MqConfig.ATES_TASKS_EXCHANGE, EventCategory.BUSINESS),

    ATES_TASK_PRICE_SET(MqConfig.ATES_TASKS_PRICES_EXCHANGE, EventCategory.BUSINESS);

    final String exchangeName;
    final EventCategory eventCategory;

    AppEventType(String exchangeName, EventCategory eventCategory) {
        this.exchangeName = exchangeName;
        this.eventCategory = eventCategory;
    }


}
