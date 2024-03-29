package org.go.together.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDto {
    private PageDto page;
    private Map<String, FilterDto> filters;
    private String mainIdField;
}
