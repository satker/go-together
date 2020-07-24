package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPhotoDto implements ComparableDto {
    private UUID id;
    private UUID groupId;
    private PhotoCategory category;
    @ComparingField(value = "photos", idCompare = true)
    private Set<PhotoDto> photos;
}
