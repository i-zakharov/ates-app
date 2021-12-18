package ru.zim.ates.common.messaging.consumer;

import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface IncomingEventRepository extends CrudRepository<IncomingEvent, Long> {
    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT a FROM IncomingEvent a WHERE a.id = :id")
    Optional<IncomingEvent> findAndLockById(Long id);
}
