package org.go.together.notification.repository;

import org.go.together.model.Notification;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.sql.SqlOperator;
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
