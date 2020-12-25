package org.go.together.compare;

import lombok.Builder;
import lombok.Getter;
import org.go.together.dto.Dto;

import java.lang.reflect.Type;
import java.util.function.Function;

@Builder(toBuilder = true)
@Getter
public class ComparingObject {
    private final Function<? extends Dto, Object> fieldValueGetter;
    private final Boolean isDeepCompare;
    private final Boolean ignored;
    private final Boolean idCompare;
    private final Type clazzType;

    public <T extends Dto> Function<T, Object> getFieldValueGetter() {
        return (Function<T, Object>) fieldValueGetter;
    }
}
