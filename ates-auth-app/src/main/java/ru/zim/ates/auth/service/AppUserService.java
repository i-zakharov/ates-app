package ru.zim.ates.auth.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.auth.dto.AppUserCreateRequestDto;
import ru.zim.ates.auth.dto.AppUserUpdateRequestDto;
import ru.zim.ates.auth.mapper.AppUserMapper;
import ru.zim.ates.auth.model.AppUser;
import ru.zim.ates.auth.repository.AppUserRepository;
import ru.zim.ates.auth.service.events.UsersProducerNotifyEvent;
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

    @Transactional
    public List<AppUser> findAll() {
        return userRepository.findAll();
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
        applicationEventPublisher.publishEvent(new UsersProducerNotifyEvent(this, eventEnvelope));
        return appUser;
    }

    @Transactional
    public AppUser update(AppUserUpdateRequestDto dto) {
        AppUser appUser = userRepository.findById(dto.getId()).orElseThrow(() -> new AppException("Запись не найдена"));
        AppUser appUserBefore = appUser.toBuilder().build();
        userMapper.mergeFromUpdateDto(dto, appUser);
        userRepository.save(appUser);
        return appUser;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

}
