package org.go.together.validation.dto;

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
