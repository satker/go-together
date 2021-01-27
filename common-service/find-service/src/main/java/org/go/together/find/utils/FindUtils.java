package org.go.together.find.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
        String[] splitWithoutGroup = string.split(DOT + GROUP_START);
        List<String> result = new LinkedList<>(Arrays.asList(splitWithoutGroup[0].split(DOT)));
        if (splitWithoutGroup.length == 2) {
            result.add("[" + splitWithoutGroup[1]);
        }
        return result.toArray(new String[0]);
    }

    public static String[] getHavingCondition(String string) {
        return string.split(HAVING_COUNT);
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
