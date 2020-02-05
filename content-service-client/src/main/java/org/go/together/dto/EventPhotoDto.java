package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPhotoDto implements Dto {
    private UUID id;
    private UUID eventId;
    private Set<PhotoDto> photos;
}
