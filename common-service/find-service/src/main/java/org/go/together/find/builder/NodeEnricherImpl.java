package org.go.together.find.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.FilterValueDto;
import org.go.together.enums.FindOperator;
import org.go.together.enums.SqlPredicate;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

import static org.go.together.find.utils.FindUtils.isRemoteField;

@Component
public class NodeEnricherImpl implements NodeEnricher {
    public void enrich(FilterNodeBuilder filterNodeBuilder,
                       String fieldOrOperator,
                       Map<String, FilterValueDto> filterValues) {
        if (isOperator(fieldOrOperator)) {
            enrichConditionNode(filterNodeBuilder, fieldOrOperator);
        } else {
            enrichFilterNode(filterNodeBuilder, fieldOrOperator, filterValues);
        }
    }

    private void enrichConditionNode(FilterNodeBuilder filterNodeBuilder, String fieldOrOperator) {
        SqlPredicate sqlPredicate = getPredicate(fieldOrOperator);
        filterNodeBuilder.condition(sqlPredicate);
    }

    private void enrichFilterNode(FilterNodeBuilder filterNodeBuilder, String fieldOrOperator, Map<String, FilterValueDto> filterValues) {
        if (isRemoteField(fieldOrOperator)) {
            enrichRemoteFilterNode(filterNodeBuilder, fieldOrOperator, filterValues);
            return;
        }
        enrichLocalFilterNode(filterNodeBuilder, fieldOrOperator, filterValues);
    }

    private void enrichLocalFilterNode(FilterNodeBuilder filterNodeBuilder, String fieldOrOperator, Map<String, FilterValueDto> filterValues) {
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

    private void enrichRemoteFilterNode(FilterNodeBuilder filterNodeBuilder, String fieldOrOperator, Map<String, FilterValueDto> filterValues) {
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setFilterType(FindOperator.IN);
        filterValueDto.setValue(filterValues);
        filterNodeBuilder.filter(fieldOrOperator, filterValueDto);
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
