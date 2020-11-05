package org.go.together.repository.sql;

import org.go.together.enums.SqlOperator;

public class OperatorParser {
    public static String getOperatorFunction(SqlOperator operator, String field, String val) {
        return switch (operator) {
            case IN -> field + " in " + val;
            case NOT_IN -> field + " not in " + val;
            case LIKE -> field + " like '%" + val.replaceAll("'", "") + "%'";
            case LIKE_LOWER_CASE -> "lower(" + field + ") like '%" + val.replaceAll("'", "") + "%'";
            case NOT_LIKE -> field + " not like '%" + val.replaceAll("'", "") + "%'";
            case EQUAL -> field + " = " + val;
            case NOT_EQUAL -> field + " <> " + val;
            case LESS -> field + " < " + val;
            case LESS_OR_EQUAL -> field + " <= " + val;
            case GREATER -> field + " > " + val;
            case GREATER_OR_EQUAL -> field + " >= " + val;
            case EMPTY_COLLECTION -> field + " is empty ";
            case NOT_EMPTY_COLLECTION -> field + " is not empty ";
            case NEAR_LOCATION -> getQueryForLocationSearch(field, val);
        };
    }

    private static String getQueryForLocationSearch(String field, String val) {
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
    }
}
