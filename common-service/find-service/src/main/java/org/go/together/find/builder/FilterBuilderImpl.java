package org.go.together.find.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.FilterDto;
import org.go.together.dto.FilterValueDto;
import org.go.together.enums.SqlPredicate;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FilterBuilderImpl implements FilterBuilder {

    @Override
    public Collection<FilterNodeBuilder> getBuilders(Map.Entry<String, FilterDto> filter) {
        return filter.getValue().getValues().stream()
                .map(value -> getBuilder(filter.getKey(), value))
                .collect(Collectors.toSet());
    }

    private FilterNodeBuilder getBuilder(String field, Map<String, FilterValueDto> filterValues) {
        FilterNodeBuilder filterNodeBuilder = new FilterNodeBuilder();
        FieldDto fieldWrapper = new FieldDto(field);
        fieldWrapper.getFieldsAndOperators()
                .forEach(fieldOrOperator -> enrichFilterNodeBuilder(filterNodeBuilder, fieldOrOperator, filterValues));
        return filterNodeBuilder;
    }

    private void enrichFilterNodeBuilder(FilterNodeBuilder filterNodeBuilder,
                                         String fieldOrOperator,
                                         Map<String, FilterValueDto> filterValues) {
        if (isOperator(fieldOrOperator)) {
            SqlPredicate sqlPredicate = getPredicate(fieldOrOperator);
            filterNodeBuilder.condition(sqlPredicate);
        } else {
            Optional<FilterValueDto> valueDto = filterValues.entrySet().stream()
                    .filter(stringFilterValueDtoEntry -> StringUtils.containsIgnoreCase(fieldOrOperator, stringFilterValueDtoEntry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst();
            if (valueDto.isEmpty()) {
                return;
            }
            FilterValueDto filterValueDto = valueDto.get();
            filterNodeBuilder.filter(fieldOrOperator, filterValueDto);
        }
    }

    private SqlPredicate getPredicate(String predicate) {
        return switch (predicate) {
            case "|" -> SqlPredicate.OR;
            case "&" -> SqlPredicate.AND;
            default -> throw new IncorrectFindObject("Sql predicates should be | or &, but was : " + predicate);
        };
    }

    private boolean isOperator(String field) {
        return field.equals("|") || field.equals("&");
    }
}
