package org.go.together.repository.impl;

import org.go.together.dto.MessageType;
import org.go.together.model.Message;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.MessageRepository;
import org.go.together.repository.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class MessageRepositoryImpl extends CustomRepositoryImpl<Message> implements MessageRepository {
    @Override
    public Collection<Message> findReviewsByRecipientId(UUID recipientId, MessageType messageType) {
        return createQuery()
                .where(createWhere()
                        .condition("recipientId", SqlOperator.EQUAL, recipientId)
                        .and()
                        .condition("messageType", SqlOperator.EQUAL, messageType)).fetchAll();
    }

    @Override
    public Collection<Message> findReviewsByEventId(UUID recipientId, MessageType messageType) {
        return createQuery()
                .where(createWhere()
                        .group(createGroup().condition("recipientId", SqlOperator.EQUAL, recipientId)
                                .or()
                                .condition("authorId", SqlOperator.EQUAL, recipientId))
                        .and()
                        .condition("messageType", SqlOperator.EQUAL, messageType)).fetchAll();
    }
}
