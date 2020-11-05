package org.go.together.repository.impl;

import org.go.together.enums.SqlOperator;
import org.go.together.model.NotificationReceiver;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.NotificationReceiverRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class NotificationReceiverRepositoryImpl extends CustomRepositoryImpl<NotificationReceiver>
        implements NotificationReceiverRepository {
    @Override
    public Collection<NotificationReceiver> findByNotificationId(UUID notificationId) {
        return createQuery().where(createWhere().condition("notification.id", SqlOperator.EQUAL, notificationId))
                .build()
                .fetchAll();
    }

    @Override
    public Collection<NotificationReceiver> findByProducerId(UUID producerId) {
        return createQuery().where(createWhere().condition("notification.producerId", SqlOperator.EQUAL, producerId))
                .build()
                .fetchAll();
    }
}
