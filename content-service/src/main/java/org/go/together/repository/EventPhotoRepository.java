package org.go.together.repository;

import org.go.together.CustomRepository;
import org.go.together.model.EventPhoto;
import org.go.together.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public class EventPhotoRepository extends CustomRepository<EventPhoto> {
    @Transactional
    public Optional<EventPhoto> findByEventId(UUID eventPhotoId) {
        return createQuery().where(createWhere().condition("eventId", SqlOperator.EQUAL, eventPhotoId)).fetchOne();
    }
}
