package ru.zim.ates.common.standartimpl.consumer.user.service;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.zim.ates.common.application.exception.AppException;
import ru.zim.ates.common.messaging.consumer.PersistEventConsumer;
import ru.zim.ates.common.messaging.schemaregistry.EventEnvelope;
import ru.zim.ates.common.messaging.schemaregistry.EventSchemaRegistry;
import ru.zim.ates.common.messaging.utils.Utils;
import ru.zim.ates.common.standartimpl.consumer.user.dto.AppUserFromEventDto;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;

@Service
@Slf4j
public class UsersStreamConsumer extends PersistEventConsumer {

    @Autowired
    private EventSchemaRegistry eventSchemaRegistry;
    @Autowired
    private AppUserService userService;

    @SneakyThrows
    @Override
    protected void processMessage(EventEnvelope eventEnvelope) {

        switch (eventEnvelope.getEventType()) {
            case ATES_USER_CREATED:
                onUserCreated(eventEnvelope);
                break;
            case ATES_USER_UPDATED:
                onUserUpdated(eventEnvelope);
                break;
            default:
                throw new AppException(String.format("Unexpect event type: %s", eventEnvelope.getEventType()));
        }
    }

    @SneakyThrows
    protected AppUser onUserCreated(EventEnvelope eventEnvelope) {
        if (!"1".equals(eventEnvelope.getEventVersion())) {
            throw new AppException(String.format("Not supported event version: %s", eventEnvelope.getEventVersion()));
        }
        AppUserFromEventDto dto = Utils.mapper.readValue(eventEnvelope.getData().toString(), AppUserFromEventDto.class);
        log.info("Received AppUserFromEventDto {}", dto);
        AppUser appUser;
        try {
            appUser = userService.create(dto);

        } catch (ConstraintViolationException | DataIntegrityViolationException e) {
            //Если не удалось создать пользователя из-за того, что такой уже есть, то попробуем сделать update, и обагатить его данными из события
            appUser = userService.update(dto);
        }
        log.debug("AppUser after {}", appUser);
        return appUser;
    }

    @SneakyThrows
    protected AppUser onUserUpdated(EventEnvelope eventEnvelope) {
        if (!"1".equals(eventEnvelope.getEventVersion())) {
            throw new AppException(String.format("Not supported event version: %s", eventEnvelope.getEventVersion()));
        }
        AppUserFromEventDto dto = Utils.mapper.readValue(eventEnvelope.getData().toString(), AppUserFromEventDto.class);
        log.debug("Received AppUserFromEventDto {}", dto);
        AppUser appUser = userService.update(dto);
        log.debug("AppUser after {}", appUser);
        return appUser;
    }


}
