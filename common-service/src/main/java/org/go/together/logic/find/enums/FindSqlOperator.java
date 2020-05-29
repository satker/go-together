package org.go.together.logic.find.enums;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.logic.repository.builder.WhereBuilder;
import org.go.together.logic.repository.utils.sql.SqlOperator;

import java.util.function.BiConsumer;

public enum FindSqlOperator {
    LIKE((entry, whereBuilder) ->
            whereBuilder.condition(entry.getKey(), SqlOperator.LIKE, entry.getValue())),
    EQUAL((entry, whereBuilder) ->
            whereBuilder.condition(entry.getKey(), SqlOperator.EQUAL, entry.getValue())),
    START_DATE((entry, whereBuilder) ->
            whereBuilder.condition(entry.getKey(), SqlOperator.GREATER_OR_EQUAL, entry.getValue())),
    END_DATE((entry, whereBuilder) ->
            whereBuilder.condition(entry.getKey(), SqlOperator.LESS_OR_EQUAL, entry.getValue())),
    IN((entry, whereBuilder) ->
            whereBuilder.condition(entry.getKey(), SqlOperator.IN, entry.getValue()));

    private final BiConsumer<Pair<String, Object>, WhereBuilder> getSearchObjectFromDtos;

    FindSqlOperator(BiConsumer<Pair<String, Object>, WhereBuilder> getSearchObjectFromDtos) {
        this.getSearchObjectFromDtos = getSearchObjectFromDtos;
    }

    public BiConsumer<Pair<String, Object>, WhereBuilder> getSearchObjectFromDtos() {
        return getSearchObjectFromDtos;
    }
}
