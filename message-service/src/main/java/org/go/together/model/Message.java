package org.go.together.model;

import lombok.Data;
import org.go.together.dto.MessageType;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "message", schema = "message_service")
public class Message implements IdentifiedEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String message;
    private Date date;
    private double rating;

    @Column(columnDefinition = "uuid")
    private UUID authorId;
    private MessageType messageType;
    private UUID recipientId;
}
