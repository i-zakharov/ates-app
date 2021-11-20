package ru.zim.ates.common.standartimpl.consumer.user.service;

import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zim.ates.common.model.AppRole;
import ru.zim.ates.common.standartimpl.consumer.user.dto.AppUserFromEventDto;
import ru.zim.ates.common.standartimpl.consumer.user.mapper.AppUserMapper;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;
import ru.zim.ates.common.standartimpl.consumer.user.repository.AppUserRepository;


@Service
public class AppUserService {
    @Autowired
    private AppUserRepository repository;
    @Autowired
    private AppUserMapper userMapper;
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public AppUser create(AppUserFromEventDto dto) {
        AppUser appUser = userMapper.fromEventDto(dto);
        repository.save(appUser); //Если такая запись уже есть то будет исключение нарушения уникальности public Id
        return appUser;
    }

    @Transactional
    public AppUser update(AppUserFromEventDto dto) {
        AppUser appUser = repository.findAndLockByPublicId(UUID.fromString(dto.getPublicId())).orElse(null);
        if (appUser == null) {
            return create(dto);
        }
        appUser = repository.findById(appUser.getId()).get();
        if (dto.getVersion() < ObjectUtils.firstNonNull(appUser.getVersion(), 0)) { //Если мы получили более старую версию объекта, то нам не не нужно обновлять данные
            return appUser;
        } else {
            userMapper.mergeFromEventDto(dto, appUser);
            appUser = repository.save(appUser);
            return appUser;
        }
    }

    @Transactional
    public AppUser changeRole (String userPublicId, String roleName, int version) {
        UUID appUserPublicId = UUID.fromString(userPublicId);
        AppRole appRole = AppRole.valueOf(roleName);
        AppUser appUser = repository.findAndLockByPublicId(appUserPublicId).orElse(null);
        if (appUser == null) {
            appUser = new AppUser();
            appUser.setRoleChangedVersion(version);
            appUser.setRole(appRole);
            appUser.setPublicId(appUserPublicId);
            return repository.save(appUser);
        }
        appUser = repository.findById(appUser.getId()).get();
        if (version < ObjectUtils.firstNonNull(appUser.getRoleChangedVersion(), 0)) { //Если мы получили более старую версию объекта, то нам не не нужно обновлять данные
            return appUser;
        } else {
            appUser.setRole(appRole);
            appUser.setRoleChangedVersion(version);
            appUser = repository.save(appUser);
            return appUser;
        }
    }

}
