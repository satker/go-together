package org.go.together.repository;

import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.Notification;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRepository extends CustomRepository<Notification> {
    public Collection<Notification> getReceiverNotifications(UUID receiverId) {
        return super.createQuery().where(createWhere()
                .condition("notificationReceivers.userId", SqlOperator.EQUAL, receiverId)).fetchAll();
    }

    public Optional<Notification> findByProducerId(UUID producerId) {
        return createQuery().where(createWhere().condition("producerId", SqlOperator.EQUAL, producerId))
                .fetchOne();
    }
}
