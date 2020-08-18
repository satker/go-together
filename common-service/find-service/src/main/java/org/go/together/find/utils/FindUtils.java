package org.go.together.find.utils;

import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FindUtils {
    public static final String DELIMITER = "\\?";
    public static final String HAVING_COUNT = ":";
    public static final String GROUP_AND = "&";
    public static final String GROUP_OR = "|";
    private static final String GROUP_START = "\\[";
    private static final String GROUP_END = "]";
    public static final String REGEX_GROUP = "^\\[[a-zA-Z&|.,]*]$";
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

    public static Map<String, FieldMapper> getFieldMapperByRemoteField(Map<String, FieldMapper> availableFields, FieldDto fieldDto) {
        String localEntityField = fieldDto.getPaths()[0];
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

    public static String[] getSingleGroupFields(String localEntityField) {
        String[] result;
        localEntityField = getHavingCondition(localEntityField)[0];
        if (localEntityField.matches(REGEX_GROUP)) {
            result = localEntityField.replaceFirst(GROUP_START, "")
                    .replaceFirst(GROUP_END, "")
                    .split("\\|" + GROUP_OR + GROUP_AND);
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
