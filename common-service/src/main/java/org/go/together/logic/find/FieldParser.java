package org.go.together.logic.find;

import org.go.together.exceptions.IncorrectFindObject;

import java.util.Map;

public class FieldParser {
    private static String[] getParsedString(String string) {
        return string.split("&");
    }

    public static String getLocalEntityField(String string) {
        return getParsedString(string)[0];
    }

    public static String getRemoteFieldToSearch(String string) {
        return getParsedString(string)[1];
    }

    public static String getRemoteGetField(String string) {
        return getParsedString(string)[2];
    }

    public static String getServiceName(String string) {
        return getRemoteFieldToSearch(string).split("\\.", 2)[0];
    }

    public static FieldMapper getFieldMapper(Map<String, FieldMapper> availableFields, String searchField) {
        String localEntityField = getLocalEntityField(searchField.split("\\.")[0]);
        FieldMapper fieldMapper = availableFields.get(localEntityField);
        if (fieldMapper == null) {
            throw new IncorrectFindObject("Field " + searchField + " is unavailable for search.");
        }
        return fieldMapper;
    }

    public static String getFieldSearch(String string) {
        return getParsedString(string)[1];
    }

    public static boolean isFieldParsed(String string) {
        return getParsedString(string).length == 2;
    }
}
