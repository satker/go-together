package org.go.together.repository.impl;

import org.go.together.model.NotificationReceiver;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.NotificationReceiverRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class NotificationReceiverRepositoryImpl extends CustomRepositoryImpl<NotificationReceiver>
        implements NotificationReceiverRepository {
    @Override
    public Collection<NotificationReceiver> findByNotificationId(UUID notificationId) {
        return createQuery().where(createWhere().condition("notification.id", SqlOperator.EQUAL, notificationId)).fetchAll();
    }

    @Override
    public Collection<NotificationReceiver> findByProducerId(UUID producerId) {
        return createQuery().where(createWhere().condition("notification.producerId", SqlOperator.EQUAL, producerId)).fetchAll();
    }
}