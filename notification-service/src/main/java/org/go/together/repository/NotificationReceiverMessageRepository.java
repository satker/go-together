package org.go.together.repository;

import org.go.together.model.NotificationReceiverMessage;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class NotificationReceiverMessageRepository extends CustomRepository<NotificationReceiverMessage> {
    public Collection<NotificationReceiverMessage> findByReceiverId(UUID receiverId) {
        return createQuery()
                .where(createWhere().condition("notificationReceiver.userId", SqlOperator.EQUAL, receiverId))
                .fetchAll();
    }
}
