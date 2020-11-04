package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationDto extends Dto {
    private UUID producerId;
}
