package org.go.together.repository.impl;

import org.go.together.enums.SqlOperator;
import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.NotificationReceiverMessageRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class NotificationReceiverMessageRepositoryImpl extends CustomRepositoryImpl<NotificationReceiverMessage>
        implements NotificationReceiverMessageRepository {
    @Override
    public Collection<NotificationReceiverMessage> findByReceiverId(UUID receiverId) {
        return createQuery()
                .where(createWhere().condition("notificationReceiver.userId", SqlOperator.EQUAL, receiverId))
                .build()
                .fetchAll();
    }

    @Override
    public Collection<NotificationReceiverMessage> findByNotificationId(UUID notificationId) {
        return createQuery()
                .where(createWhere().condition("notificationReceiver.notification.id", SqlOperator.EQUAL, notificationId))
                .build()
                .fetchAll();
    }
}
