package org.go.together.dto;

import lombok.Data;

@Data
public class ComparingObject {
    private final Object fieldValue;
    private final Boolean isDeepCompare;
    private final Boolean isMain;

    public ComparingObject(Object fieldValue, Boolean isDeepCompare, Boolean isMain) {
        this.fieldValue = fieldValue;
        this.isDeepCompare = isDeepCompare;
        this.isMain = isMain;
    }
}
