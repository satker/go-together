package org.go.together.dto.filter;

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
    private Boolean isMappingNeed;

    public FormDto(PageDto page, Map<String, FilterDto> filters, String mainIdField) {
        this.page = page;
        this.filters = filters;
        this.mainIdField = mainIdField;
        this.isMappingNeed = true;
    }
}
