package org.go.together.repository;

import org.go.together.model.NotificationReceiver;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class NotificationReceiverRepository extends CustomRepository<NotificationReceiver> {
    public Collection<NotificationReceiver> findByNotificationId(UUID notificationId) {
        return createQuery().where(createWhere().condition("notification.id", SqlOperator.EQUAL, notificationId)).fetchAll();
    }

    public Collection<NotificationReceiver> findByProducerId(UUID producerId) {
        return createQuery().where(createWhere().condition("notification.producerId", SqlOperator.EQUAL, producerId)).fetchAll();
    }
}
