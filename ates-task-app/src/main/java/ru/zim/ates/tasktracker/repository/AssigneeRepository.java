package ru.zim.ates.tasktracker.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import ru.zim.ates.common.standartimpl.consumer.user.model.AppUser;

public interface AssigneeRepository extends CrudRepository<AppUser, Long> {

    List<AppUser> findByIsActiveIsTrue();

}
