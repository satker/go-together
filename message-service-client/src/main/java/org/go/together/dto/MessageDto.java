package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.go.together.interfaces.Dto;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data
public class MessageDto implements Dto {
    private UUID id;
    private String message;
    private Double rating;
    private Date date;
    private UUID authorId;
    private MessageType messageType;
    private UUID recipientId;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("message", ComparingObject.builder().getDtoField(this::getMessage).build())
                .put("rating", ComparingObject.builder().getDtoField(this::getRating).build())
                .put("date", ComparingObject.builder().getDtoField(this::getDate).build())
                .put("messageType", ComparingObject.builder().getDtoField(this::getMessageType).build())
                .build();
    }
}
