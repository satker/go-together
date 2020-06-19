package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventPhotoDto implements Dto {
    private UUID id;
    private UUID eventId;
    private Set<PhotoDto> photos;

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("photos", ComparingObject.builder().getDtoField(this::getPhotos).build())
                .build();
    }
}
