package org.go.together.find.builder;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.FilterDto;
import org.go.together.dto.FilterValueDto;
import org.go.together.find.dto.Field;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FilterBuilderImpl implements FilterBuilder {
    private final NodeEnricher nodeEnricher;

    @Override
    public Collection<FilterNodeBuilder> getBuilders(Map.Entry<String, FilterDto> filter) {
        return filter.getValue().getValues().stream()
                .map(value -> getBuilder(filter.getKey(), value))
                .collect(Collectors.toSet());
    }

    private FilterNodeBuilder getBuilder(String field, Map<String, FilterValueDto> filterValues) {
        FilterNodeBuilder filterNodeBuilder = new FilterNodeBuilder();
        Field fieldWrapper = new Field(field);
        fieldWrapper.getFieldsAndOperators()
                .forEach(fieldOrOperator -> nodeEnricher.enrich(filterNodeBuilder, fieldOrOperator, filterValues));
        return filterNodeBuilder;
    }


}
