package org.go.together.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;

import java.util.UUID;

@EqualsAndHashCode(callSuper = false)
@Data
public class LanguageDto extends ComparableDto {
    private UUID id;
    @ComparingField("name")
    private String name;
    @ComparingField("code")
    private String code;

    @Override
    public String getMainField() {
        return name;
    }
}
