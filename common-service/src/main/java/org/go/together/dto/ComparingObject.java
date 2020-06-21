package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.function.Supplier;

@Data
@Builder
@AllArgsConstructor
public class ComparingObject {
    @Builder.Default
    private Boolean isNeededDeepCompare = true;
    private Supplier<?> getDtoField;
    @Builder.Default
    private Boolean isMain = false;
}
