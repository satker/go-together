package org.go.together.dto;

import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.Dto;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto implements Dto {
    private UUID id;
    private String photoUrl;
    private PhotoCategory photoCategory;
    private ContentDto content;

    public PhotoDto(UUID id) {
        this.id = id;
    }

    @Override
    public Map<String, ComparingObject> getComparingMap() {
        return ImmutableMap.<String, ComparingObject>builder()
                .put("photo url", ComparingObject.builder().getDtoField(this::getPhotoUrl).build())
                .put("photo category", ComparingObject.builder().getDtoField(this::getPhotoCategory).build())
                .put("content", ComparingObject.builder().getDtoField(this::getContent).isNeededDeepCompare(false).build())
                .build();
    }
}
