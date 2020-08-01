package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;

import java.util.UUID;

@Data
public class LanguageDto implements Dto {
    private UUID id;
    @ComparingField(value = "name", isMain = true)
    private String name;
    @ComparingField("code")
    private String code;
}
