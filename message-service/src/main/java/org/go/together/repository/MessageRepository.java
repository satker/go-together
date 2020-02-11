package org.go.together.repository;

import org.go.together.dto.MessageType;
import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.CustomSqlBuilder;
import org.go.together.logic.repository.utils.sql.SqlOperator;
import org.go.together.model.Message;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
    public Collection<Message> findMessagesBetweenUsers(UUID myId, UUID otherUser) {
        CustomSqlBuilder.WhereBuilder whereMyIdPresented = createWhere().condition("authorId", SqlOperator.EQUAL, myId)
                .or()
                .condition("recipientId", SqlOperator.EQUAL, myId);
        CustomSqlBuilder.WhereBuilder whereUserIdPresented = createWhere().condition("authorId", SqlOperator.EQUAL, otherUser)
                .or()
                .condition("recipientId", SqlOperator.EQUAL, otherUser);
        return createQuery()
                .where(createWhere().condition("messageType", SqlOperator.EQUAL, TO_USER)
                        .group(whereMyIdPresented)
                        .and()
                        .group(whereUserIdPresented))
                .fetchAll();
    }

    public Map<UUID, List<Message>> findLastMessagesForUserId(UUID myId) {
        Collection<Message> messages = createQuery().groupingByLastRow("authorId", "date",
                createWhere().condition("messageType", SqlOperator.EQUAL, TO_USER)).fetchAll();
        return messages.stream().collect(Collectors.groupingBy(Message::getRecipientId));
    }
}
