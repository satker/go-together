package org.go.together.find.builder;

import org.go.together.dto.FilterValueDto;
import org.go.together.find.dto.node.FilterNodeBuilder;

import java.util.Map;

public interface NodeEnricher {
    void enrich(FilterNodeBuilder filterNodeBuilder,
                String fieldOrOperator,
                Map<String, FilterValueDto> filterValues);
}
