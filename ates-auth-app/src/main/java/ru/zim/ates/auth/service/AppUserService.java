package ru.zim.ates.auth.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.auth.dto.AppUserCreateRequestDto;
import ru.zim.ates.auth.dto.AppUserUpdateRequestDto;
import ru.zim.ates.auth.mapper.AppUserMapper;
import ru.zim.ates.auth.model.AppUser;
import ru.zim.ates.auth.repository.AppUserRepository;
import ru.zim.ates.auth.service.events.ProducerNotifyEvent;
import ru.zim.ates.common.exception.AppException;
import ru.zim.ates.common.schemaregistry.EventEnvelope;
import ru.zim.ates.common.schemaregistry.EventType;

import static ru.zim.ates.common.schemaregistry.MqConfig.ATES_AUTH_PRODUCER_NAME;

@Service
public class AppUserService {
    @Autowired
    AppUserRepository userRepository;
    @Autowired
    AppUserMapper userMapper;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<AppUser> findAll() {
        return userRepository.findAll();
    }

    @Transactional
    public Optional<AppUser> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public Optional<AppUser> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public AppUser create(AppUserCreateRequestDto dto) {
        AppUser appUser = userMapper.fromCreateDto(dto);
        userRepository.save(appUser);
        //Создаем событие для отправки в другие сервисы
        EventEnvelope eventEnvelope = EventEnvelope.preSetBuilder()
                .eventType(EventType.ATES_USER_CREATED)
                .producer(ATES_AUTH_PRODUCER_NAME)
                .eventVersion("1")
                .data(userMapper.toResponceDto(appUser)).build();
        //TODO здесь можно добавить сохранение события в БД, тогда отправку можно будет вызвать вне транзакции и асинхронно
        //Уведомляем отправщик, что надо отправить сообщение
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, eventEnvelope));
        //Также отправим сообщение, что пользователю назначена роль
        EventEnvelope roleChangedEvent = buildRoleChangedEvent(appUser);
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, roleChangedEvent));
        return appUser;
    }

    @Transactional
    public AppUser update(AppUserUpdateRequestDto dto) {
        AppUser appUser = userRepository.findById(dto.getId()).orElseThrow(() -> new AppException("Запись не найдена"));
        AppUser appUserBefore = appUser.toBuilder().build();
        userMapper.mergeFromUpdateDto(dto, appUser);
        appUser = userRepository.save(appUser);
        entityManager.flush(); //пока не зафлашим изменения, версия объекта не поменяется.
        // По хорошему для нашей задачи лучше реализовать версию руками, а не через @Version , т.к. @Version по хорошумя для другого

        EventEnvelope updateEvent = EventEnvelope.preSetBuilder()
                .eventType(EventType.ATES_USER_UPDATED)
                .producer(ATES_AUTH_PRODUCER_NAME)
                .eventVersion("1")
                .data(userMapper.toResponceDto(appUser)).build();
        applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, updateEvent));
        if (!Objects.equals(appUser.getRole(), appUserBefore.getRole())) {
            EventEnvelope roleChangedEvent = buildRoleChangedEvent(appUser);
            applicationEventPublisher.publishEvent(new ProducerNotifyEvent(this, roleChangedEvent));
        }
        return appUser;
    }

    @Transactional
    public void delete(AppUser appUser) {
        userRepository.delete(appUser);
    }

    private EventEnvelope buildRoleChangedEvent(AppUser appUser) {
        Map<String, Object> data = new HashMap<>();
        data.put("publicId", appUser.getPublicId());
        data.put("role", appUser.getRole());
        data.put("version", appUser.getVersion());
        return EventEnvelope.preSetBuilder()
                .eventType(EventType.ATES_USER_ROLE_CHANGED)
                .producer(ATES_AUTH_PRODUCER_NAME)
                .eventVersion("1")
                .data(data).build();
    }

}
