package org.go.together.repository.builder.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class WhereDto {
    private final StringBuilder join;
    private final StringBuilder whereQuery;
}
