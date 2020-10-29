package org.go.together.dto;

import lombok.Data;
import org.go.together.interfaces.ComparableDto;

import java.util.function.Function;

@Data
public class ComparingObject {
    private final Function<? extends ComparableDto, Object> fieldValueGetter;
    private final Boolean isDeepCompare;
    private final Boolean isMain;
    private final Boolean ignored;
    private final Boolean idCompare;

    public ComparingObject(Function<? extends ComparableDto, Object> fieldValueGetter,
                           Boolean isDeepCompare,
                           Boolean isMain,
                           Boolean ignored,
                           Boolean idCompare) {
        this.fieldValueGetter = fieldValueGetter;
        this.isDeepCompare = isDeepCompare;
        this.isMain = isMain;
        this.ignored = ignored;
        this.idCompare = idCompare;
    }

    public <T extends ComparableDto> Function<T, Object> getFieldValueGetter() {
        return (Function<T, Object>) fieldValueGetter;
    }
}
