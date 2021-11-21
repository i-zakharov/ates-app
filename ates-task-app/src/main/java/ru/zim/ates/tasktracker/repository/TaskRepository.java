package ru.zim.ates.tasktracker.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;
import ru.zim.ates.tasktracker.model.Task;
import ru.zim.ates.tasktracker.model.TaskStatus;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Optional<Task> findById(Long id);

    List<Task> findAll();

    List<Task> findByStatus(TaskStatus status);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT a FROM Task a WHERE a.publicId = :publicId")
    Optional<Task> findAndLockByPublicId(UUID publicId);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT a FROM Task a WHERE a.id = :id")
    Optional<Task> findAndLockById(Long id);
}
