package org.go.together.repository.sql;

import java.util.function.BiFunction;

public enum SqlOperator {
    IN((field, val) -> field + " in " + val),
    NOT_IN((field, val) -> field + " not in " + val),
    LIKE((field, val) -> field + " like '%" + val.replaceAll("'", "") + "%'"),
    LIKE_LOWER_CASE((field, val) -> "lower(" + field + ") like '%" + val.replaceAll("'", "") + "%'"),
    NOT_LIKE((field, val) -> field + " not like '%" + val.replaceAll("'", "") + "%'"),
    EQUAL((field, val) -> field + " = " + val),
    NOT_EQUAL((field, val) -> field + " <> " + val),
    LESS((field, val) -> field + " < " + val),
    LESS_OR_EQUAL((field, val) -> field + " <= " + val),
    GREATER((field, val) -> field + " > " + val),
    GREATER_OR_EQUAL((field, val) -> field + " >= " + val),
    EMPTY_COLLECTION((field, val) -> field + " is empty "),
    NOT_EMPTY_COLLECTION((field, val) -> field + " is not empty "),
    NEAR_LOCATION((field, val) -> {
        val = val.replaceAll("'", "");
        String[] entityLinkLatLngField = field.split("\\.");
        String[] latitudeLongitudeValue = val.split(",");
        String[] latitudeLongitudeField = entityLinkLatLngField[1].split(",");
        String latitudeField = entityLinkLatLngField[0] + "." + latitudeLongitudeField[0];
        String latitudeValue = latitudeLongitudeValue[0];
        String longitudeField = entityLinkLatLngField[0] + "." + latitudeLongitudeField[1];
        String longitudeValue = latitudeLongitudeValue[1];
        return " SQRT(POW(69.1 * (" + latitudeField + " - " + latitudeValue +
                "), 2) + " +
                "POW(69.1 * (" + longitudeValue + " - " + longitudeField +
                ") * COS(" + latitudeField + " / 57.3), 2)) < 25 ";
    });

    private final BiFunction<String, String, String> biFunction;


    SqlOperator(BiFunction<String, String, String> biFunction) {
        this.biFunction = biFunction;
    }

    public BiFunction<String, String, String> getBiFunction() {
        return biFunction;
    }
}
