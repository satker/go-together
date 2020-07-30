package org.go.together.repository;

import org.go.together.CustomRepository;
import org.go.together.model.NotificationMessage;
import org.go.together.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class NotificationMessageRepository extends CustomRepository<NotificationMessage> {
    public Collection<NotificationMessage> findByNotificationId(UUID notificationId) {
        return createQuery().where(createWhere()
                .condition("notification.id", SqlOperator.EQUAL, notificationId)).fetchAll();
    }
}
