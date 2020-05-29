package org.go.together.logic.find.utils;

import org.go.together.dto.filter.FieldMapper;
import org.go.together.exceptions.IncorrectFindObject;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldParser {
    private static final String DELIMITER = "\\?";
    public static final String GROUP_AND = "&";
    public static final String GROUP_OR = "\\|";
    private static final String GROUP_START = "\\[";
    private static final String GROUP_END = "]";

    private static String[] getParsedString(String string) {
        return string.split(DELIMITER);
    }

    private static String[] getSplitByCommaString(String string) {
        return string.split("\\.");
    }

    public static String[] getLocalEntityField(String string) {
        Pattern pattern = Pattern.compile("^\\[[a-zA-Z&|.]*]$");
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return new String[]{matcher.group(0)};
        } else {
            return getSplitByCommaString(getParsedString(string)[0]);
        }
    }

    public static String getFieldSearch(String string) {
        String[] parsedString = getParsedString(string);
        if (parsedString.length > 1) {
            return parsedString[1];
        }
        return parsedString[0];
    }

    public static Map<String, FieldMapper> getFieldMappers(Map<String, FieldMapper> availableFields,
                                                           String searchField) {
        String[] localEntityFullFields = getLocalEntityField(searchField);
        String localEntityField = localEntityFullFields[0];
        String[] singleGroupFields = getSingleGroupFields(localEntityField);
        try {
            return Stream.of(singleGroupFields)
                    .collect(Collectors.toMap(field -> getEntityField(field, localEntityFullFields),
                            field -> getFieldMapper(availableFields, field)));
        } catch (Exception exception) {
            throw new IncorrectFindObject("Field " + searchField + " is unavailable for search.");
        }
    }

    private static FieldMapper getFieldMapper(Map<String, FieldMapper> availableFields,
                                              String field) {
        String[] splitByCommaString = getSplitByCommaString(field);
        if (splitByCommaString.length > 1) {
            return availableFields.get(splitByCommaString[0]);
        }
        return availableFields.get(field);
    }

    private static String getEntityField(String field, String[] localEntityFullFields) {
        if (localEntityFullFields.length > 1) {
            return field + "." + localEntityFullFields[1];
        } else {
            return field;
        }
    }

    public static String getLocalFieldForSearch(Map<String, FieldMapper> fieldMappers, String string) {
        fieldMappers.forEach((fieldName, fieldMapper) ->
                string.replaceAll(fieldName, fieldMapper.getCurrentServiceField()));
        return string;
    }

    public static String[] getSingleGroupFields(String localEntityField) {
        String[] result;
        if (localEntityField.matches("^\\[[a-zA-Z&|.]*]$")) {
            result = localEntityField.replaceFirst(GROUP_START, "")
                    .replaceFirst(GROUP_END, "")
                    .split(GROUP_OR + "|" + GROUP_AND);
        } else {
            result = new String[]{localEntityField};
        }
        return result;
    }

    public static String getDelimiter(String groupFields, String currentField) {
        int index = groupFields.lastIndexOf(currentField);
        String delimiter = String.valueOf(groupFields.charAt(index + currentField.length()));
        if (delimiter.equals(GROUP_AND) || delimiter.equals(GROUP_OR)) {
            return delimiter;
        }
        return null;
    }
}
