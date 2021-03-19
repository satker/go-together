package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.go.together.compare.ComparingField;

import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupPhotoDto extends Dto {
    private UUID groupId;
    private PhotoCategory category;
    @ComparingField(value = "photos", idCompare = true)
    private Collection<PhotoDto> photos;
}
