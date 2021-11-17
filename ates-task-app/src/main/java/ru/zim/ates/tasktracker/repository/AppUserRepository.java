package ru.zim.ates.tasktracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;
import ru.zim.ates.tasktracker.model.AppUser;

public interface AppUserRepository extends CrudRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findById(Long id);

    Optional<AppUser> findByPublicId(UUID id);

    List<AppUser> findAll();
}
