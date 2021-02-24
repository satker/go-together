package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilterDto {
    private Collection<Map<String, FilterValueDto>> values;
}
