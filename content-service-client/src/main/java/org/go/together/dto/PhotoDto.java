package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.interfaces.ComparableDto;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto implements ComparableDto {
    private UUID id;

    private String photoUrl;
    private PhotoCategory photoCategory;
    private ContentDto content;
}
