package org.go.together.repository;

import org.go.together.CustomRepository;
import org.go.together.model.Notification;
import org.go.together.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRepository extends CustomRepository<Notification> {
    public Optional<Notification> findByProducerId(UUID producerId) {
        return createQuery().where(createWhere().condition("producerId", SqlOperator.EQUAL, producerId))
                .fetchOne();
    }
}
