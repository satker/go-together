package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPhotoDto implements Dto {
    private UUID id;
    private UUID eventId;
    @ComparingField(value = "photos", idCompare = true)
    private Set<PhotoDto> photos;
}
