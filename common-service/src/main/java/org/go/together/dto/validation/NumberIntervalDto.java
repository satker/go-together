package org.go.together.dto.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumberIntervalDto {
    private Number number;
    private Number min;
    private Number max;
}
