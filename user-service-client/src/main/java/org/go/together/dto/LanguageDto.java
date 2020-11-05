package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingField;

@EqualsAndHashCode(callSuper = true)
@Data
public class LanguageDto extends ComparableDto {
    @ComparingField("name")
    private String name;
    @ComparingField("code")
    private String code;

    @Override
    public String getMainField() {
        return name;
    }
}
