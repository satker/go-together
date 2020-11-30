package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.go.together.enums.FindOperator;

import java.util.Collection;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {
    private FindOperator filterType;
    private Collection<Map<String, Object>> values;
}
