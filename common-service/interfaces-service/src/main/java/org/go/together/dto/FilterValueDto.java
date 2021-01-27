package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.enums.FindOperator;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterValueDto {
    private FindOperator filterType;
    private Object value;
}
