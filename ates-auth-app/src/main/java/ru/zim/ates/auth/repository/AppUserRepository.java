package ru.zim.ates.auth.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.zim.ates.auth.model.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByUsernameAndIsActiveIsTrue(String username);

    List<AppUser> findAll();
}
