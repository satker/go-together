package org.go.together.repository;

import org.go.together.dto.MessageType;
import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.builder.WhereBuilder;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.Message;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.UUID;

import static org.go.together.dto.MessageType.TO_USER;

@Repository
public class MessageRepository extends CustomRepository<Message> {
    @Transactional
    public Collection<Message> findReviewsByRecipientId(UUID recipientId, MessageType messageType) {
        return createQuery()
                .where(createWhere()
                        .condition("recipientId", SqlOperator.EQUAL, recipientId)
                        .and()
                        .condition("messageType", SqlOperator.EQUAL, messageType)).fetchAll();
    }

    @Transactional
    public Collection<Message> findReviewsByEventId(UUID recipientId, MessageType messageType) {
        return createQuery()
                .where(createWhere()
                        .group(createGroup().condition("recipientId", SqlOperator.EQUAL, recipientId)
                                .or()
                                .condition("authorId", SqlOperator.EQUAL, recipientId))
                        .and()
                        .condition("messageType", SqlOperator.EQUAL, messageType)).fetchAll();
    }

    @Transactional
    public Collection<Message> findReviewsByRecipientId(UUID recipientId, UUID authorId, MessageType messageType) {
        return createQuery()
                .where(createWhere()
                        .condition("messageType", SqlOperator.EQUAL, messageType)
                        .and()
                        .group(createGroup().condition("recipientId", SqlOperator.EQUAL, recipientId)
                                .and()
                                .condition("authorId", SqlOperator.EQUAL, authorId))
                        .or()
                        .group(createGroup().condition("recipientId", SqlOperator.EQUAL, authorId)
                                .and()
                                .condition("authorId", SqlOperator.EQUAL, recipientId))
                ).fetchAll();
    }

    @Transactional
    public Collection<Message> findMessagesBetweenUsers(UUID myId, UUID otherUser) {
        WhereBuilder<Message> whereMyIdPresented = createWhere().condition("authorId", SqlOperator.EQUAL, myId)
                .or()
                .condition("recipientId", SqlOperator.EQUAL, myId);
        WhereBuilder<Message> whereUserIdPresented = createWhere().condition("authorId", SqlOperator.EQUAL, otherUser)
                .or()
                .condition("recipientId", SqlOperator.EQUAL, otherUser);
        return createQuery()
                .where(createWhere().condition("messageType", SqlOperator.EQUAL, TO_USER)
                        .group(whereMyIdPresented)
                        .and()
                        .group(whereUserIdPresented))
                .fetchAll();
    }
}
