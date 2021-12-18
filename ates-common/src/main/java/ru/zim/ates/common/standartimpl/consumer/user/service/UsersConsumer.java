package ru.zim.ates.common.standartimpl.consumer.user.service;

import java.util.HashMap;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zim.ates.common.application.exception.AppException;
import ru.zim.ates.common.messaging.consumer.PersistEventConsumer;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;
import ru.zim.ates.common.messaging.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.messaging.utils.Utils;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;

@Service
@Slf4j
public class UsersConsumer extends PersistEventConsumer {

    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;
    @Autowired
    private AppUserService userService;

    @SneakyThrows
    @Override
    protected void processMessage(EventEnvelope eventEnvelope) {
        switch (eventEnvelope.getEventType()) {
            case ATES_USER_ROLE_CHANGED:
                onUserRoleChanged(eventEnvelope);
                break;
            default:
                throw new AppException(String.format("Unexpect event type: %s", eventEnvelope.getEventType()));
        }
    }

    @SneakyThrows
    protected AppUser onUserRoleChanged(EventEnvelope eventEnvelope) {
        if (!"1".equals(eventEnvelope.getEventVersion())) {
            throw new AppException(String.format("Not supported event version: %s", eventEnvelope.getEventVersion()));
        }
        Map<String, Object> eventFieldsMap = Utils.mapper.readValue(eventEnvelope.getData().toString(), HashMap.class);
        String publicId = eventFieldsMap.get("publicId").toString();
        String role = eventFieldsMap.get("role").toString();
        int version = Integer.parseInt(eventFieldsMap.get("version").toString());
        AppUser appUser = userService.changeRole(publicId, role, version);
        log.debug("AppUser after {}", appUser);
        return appUser;
    }

}
