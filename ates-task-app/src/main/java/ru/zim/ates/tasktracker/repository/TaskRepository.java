package ru.zim.ates.tasktracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.zim.ates.tasktracker.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Optional<Task> findById(Long id);

    List<Task> findAll();
}
