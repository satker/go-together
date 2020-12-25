package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationDto extends Dto {
    private UUID producerId;
}
