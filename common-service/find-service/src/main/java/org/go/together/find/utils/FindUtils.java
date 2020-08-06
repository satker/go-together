package org.go.together.find.utils;

import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.dto.FieldMapper;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FindUtils {
    public static final String DELIMITER = "\\?";
    public static final String HAVING_COUNT = ":";
    public static final String GROUP_AND = "&";
    public static final String GROUP_OR = "\\|";
    private static final String GROUP_START = "\\[";
    private static final String GROUP_END = "]";
    private static final String REGEX_GROUP = "^\\[[a-zA-Z&|.,]*]$";
    public static final String DOT = "\\.";

    public static String[] getParsedRemoteField(String string) {
        return string.split(DELIMITER);
    }

    public static String[] getParsedFields(String string) {
        return string.split(DOT);
    }

    public static String[] getHavingCondition(String string) {
        return string.split(HAVING_COUNT);
    }

    public static String[] getPathFields(String string) {
        Pattern pattern = Pattern.compile(REGEX_GROUP);
        Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return new String[]{matcher.group(0)};
        } else {
            return getParsedFields(getParsedRemoteField(string)[0]);
        }
    }

    public static String getAnotherServicePathFields(String string) {
        String[] otherServiceFields = getParsedRemoteField(string);
        if (otherServiceFields.length > 1) {
            return otherServiceFields[1];
        }
        return null;
    }

    public static String getFieldSearch(String string) {
        String[] parsedString = getParsedRemoteField(string);
        if (parsedString.length > 1) {
            return parsedString[1];
        }
        return parsedString[0];
    }

    public static Map<String, FieldMapper> getFieldMapperByRemoteField(Map<String, FieldMapper> availableFields, String searchField) {
        String[] localEntityFullFields = getPathFields(searchField);
        String localEntityField = localEntityFullFields[0];
        List<String> singleGroupFields = List.of(getSingleGroupFields(localEntityField));
        return availableFields.entrySet().stream()
                .filter(stringFieldMapperEntry ->
                        singleGroupFields.contains(stringFieldMapperEntry.getValue().getCurrentServiceField()))
                .collect(Collectors.toMap(entry ->
                                findStringFromList(entry.getValue().getCurrentServiceField(), singleGroupFields),
                        Map.Entry::getValue,
                        (entry1, entry2) -> entry1));
    }

    private static String findStringFromList(String element, List<String> elements) {
        return elements.stream()
                .filter(string -> string.equals(element))
                .findFirst().orElseThrow(() ->
                        new IncorrectFindObject("Field " + element + " is unavailable for find."));
    }

    public static Map<String, FieldMapper> getFieldMappers(Map<String, FieldMapper> availableFields,
                                                           String searchField) {
        String[] localEntityFullFields = getPathFields(searchField);
        String localEntityField = localEntityFullFields[0];
        String[] singleGroupFields = getSingleGroupFields(localEntityField);
        try {
            return Stream.of(singleGroupFields)
                    .collect(Collectors.toMap(FindUtils::getEntityField,
                            field -> getFieldMapper(availableFields, field),
                            (fieldMapper, fieldMapper2) -> fieldMapper));
        } catch (Exception exception) {
            throw new IncorrectFindObject("Field " + searchField + " is unavailable for search.");
        }
    }

    private static FieldMapper getFieldMapper(Map<String, FieldMapper> availableFields,
                                              String field) {
        String[] splitByCommaString = getParsedFields(field);
        if (splitByCommaString.length > 1) {
            return availableFields.get(splitByCommaString[0]);
        }
        return availableFields.get(field);
    }

    private static String getEntityField(String field) {
        String[] splitByCommaString = getParsedFields(field);
        if (splitByCommaString.length > 1) {
            return splitByCommaString[0];
        }
        return field;
    }

    public static String[] getSingleGroupFields(String localEntityField) {
        String[] result;
        localEntityField = getHavingCondition(localEntityField)[0];
        if (localEntityField.matches(REGEX_GROUP)) {
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
