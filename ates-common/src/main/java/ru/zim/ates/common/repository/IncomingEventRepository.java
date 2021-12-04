package ru.zim.ates.common.repository;

import org.springframework.data.repository.CrudRepository;
import ru.zim.ates.common.model.IncomingEvent;

public interface IncomingEventRepository extends CrudRepository<IncomingEvent, Long> {
}
