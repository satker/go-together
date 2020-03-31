package org.go.together.model;

import lombok.Data;
import org.go.together.dto.MessageType;
import org.go.together.interfaces.IdentifiedEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "message", schema = "public")
public class Message implements IdentifiedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String message;
    private Date date;
    private double rating;
    private UUID authorId;
    private MessageType messageType;
    private UUID recipientId;
}