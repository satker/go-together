package org.go.together.repository.builder.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SqlDto {
    private final String havingCondition;
    private final String selectRow;
    private final String query;
}
