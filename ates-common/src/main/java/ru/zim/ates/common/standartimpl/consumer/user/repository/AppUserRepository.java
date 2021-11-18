package ru.zim.ates.common.standartimpl.consumer.user.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;


public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByPublicId(UUID id);

    List<AppUser> findAll();

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT a FROM AppUser a WHERE a.publicId = :publicId")
    Optional<AppUser> findAndLockByPublicId(UUID publicId);

}
