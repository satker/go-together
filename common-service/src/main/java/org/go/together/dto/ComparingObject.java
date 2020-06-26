package org.go.together.dto;

import lombok.Data;

@Data
public class ComparingObject {
    private final Object fieldValue;
    private final Boolean isDeepCompare;
    private final Boolean isMain;
    private final Boolean ignored;
    private final Boolean idCompare;

    public ComparingObject(Object fieldValue, Boolean isDeepCompare, Boolean isMain, Boolean ignored, Boolean idCompare) {
        this.fieldValue = fieldValue;
        this.isDeepCompare = isDeepCompare;
        this.isMain = isMain;
        this.ignored = ignored;
        this.idCompare = idCompare;
    }
}
