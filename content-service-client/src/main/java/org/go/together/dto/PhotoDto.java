package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.compare.ComparableDto;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhotoDto extends ComparableDto {
    private String photoUrl;
    private ContentDto content;

    @Override
    public String getMainField() {
        return StringUtils.isNotBlank(photoUrl) ? photoUrl : content.getType();
    }
}
