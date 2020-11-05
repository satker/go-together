package org.go.together.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.dto.MessageType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "message", schema = "message_service")
public class Message extends IdentifiedEntity {
    private String message;
    private Date date;
    private double rating;

    @Column(columnDefinition = "uuid")
    private UUID authorId;

    private MessageType messageType;

    @Column(columnDefinition = "uuid")
    private UUID recipientId;
}
