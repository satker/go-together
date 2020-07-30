package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto implements ComparableDto {
    private UUID id;

    @ComparingField("photo url")
    private String photoUrl;

    @ComparingField(value = "content", deepCompare = false)
    private ContentDto content;
}
