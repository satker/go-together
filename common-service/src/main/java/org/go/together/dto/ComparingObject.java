package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComparingObject {
    @Builder.Default
    private Boolean isNeededDeepCompare = true;
    private Object fieldValue;
    @Builder.Default
    private Boolean isMain = false;
}
