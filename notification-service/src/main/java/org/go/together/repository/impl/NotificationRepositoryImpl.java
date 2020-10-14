package org.go.together.repository.impl;

import org.go.together.model.Notification;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.NotificationRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class NotificationRepositoryImpl extends CustomRepositoryImpl<Notification> implements NotificationRepository {
    @Override
    public Optional<Notification> findByProducerId(UUID producerId) {
        return createQuery().where(createWhere().condition("producerId", SqlOperator.EQUAL, producerId))
                .build()
                .fetchOne();
    }
}
