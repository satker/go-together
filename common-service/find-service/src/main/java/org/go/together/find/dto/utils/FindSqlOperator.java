package org.go.together.find.dto.utils;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.repository.builder.Where;
import org.go.together.repository.sql.SqlOperator;

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
            whereBuilder.condition(entry.getKey(), SqlOperator.IN, entry.getValue())),
    NEAR_LOCATION((entry, whereBuilder) -> {
        if (entry.getKey().contains("longitude")) {
            whereBuilder.condition(entry.getKey(), SqlOperator.NEAR_LOCATION, entry.getValue());
        } else {
            whereBuilder.condition(entry.getKey(), SqlOperator.EQUAL, entry.getValue());
        }
    });

    private final BiConsumer<Pair<String, Object>, Where.WhereBuilder> getSearchObjectFromDtos;

    FindSqlOperator(BiConsumer<Pair<String, Object>, Where.WhereBuilder> getSearchObjectFromDtos) {
        this.getSearchObjectFromDtos = getSearchObjectFromDtos;
    }

    public BiConsumer<Pair<String, Object>, Where.WhereBuilder> getSearchObjectFromDtos() {
        return getSearchObjectFromDtos;
    }
}
