package org.go.together.logic.repository.utils.sql;

import java.util.function.BiFunction;

public enum SqlOperator {
    IN((field, val) -> field + " in " + val),
    NOT_IN((field, val) -> field + " not in " + val),
    LIKE((field, val) -> field + " like '%" + val.replaceAll("'", "") + "%'"),
    NOT_LIKE((field, val) -> field + " not like '%" + val.replaceAll("'", "") + "%'"),
    EQUAL((field, val) -> field + " = " + val),
    NOT_EQUAL((field, val) -> field + " <> " + val),
    LESS((field, val) -> field + " < " + val),
    LESS_OR_EQUAL((field, val) -> field + " <= " + val),
    GREATER((field, val) -> field + " > " + val),
    GREATER_OR_EQUAL((field, val) -> field + " >= " + val),
    EMPTY_COLLECTION((field, val) -> field + " is empty "),
    NOT_EMPTY_COLLECTION((field, val) -> field + " is not empty ");

    private BiFunction<String, String, String> biFunction;


    SqlOperator(BiFunction<String, String, String> biFunction) {
        this.biFunction = biFunction;
    }

    public BiFunction<String, String, String> getBiFunction() {
        return biFunction;
    }
}
