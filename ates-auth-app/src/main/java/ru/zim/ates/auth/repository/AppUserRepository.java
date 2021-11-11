package ru.zim.ates.auth.repository;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.zim.ates.auth.model.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, String> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByUsernameAndIsActiveIsTrue(String username);
}
