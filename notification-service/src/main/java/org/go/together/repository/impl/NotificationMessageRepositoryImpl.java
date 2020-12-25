package org.go.together.repository.impl;

import org.go.together.enums.SqlOperator;
import org.go.together.model.NotificationMessage;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.NotificationMessageRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class NotificationMessageRepositoryImpl extends CustomRepositoryImpl<NotificationMessage>
        implements NotificationMessageRepository {
    @Override
    public Collection<NotificationMessage> findByNotificationId(UUID notificationId) {
        return createQuery().where(createWhere()
                .condition("notification.id", SqlOperator.EQUAL, notificationId)).build().fetchAll();
    }
}
