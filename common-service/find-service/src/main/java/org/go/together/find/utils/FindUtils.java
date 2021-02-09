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
    public static final String DOT = "\\.";

    public static String[] getParsedRemoteField(String string) {
        String[] split = string.split(DELIMITER, 2);
        if (split.length == 2 && split[0].contains("[")) {
            return new String[]{string};
        }
        return split;
    }

    public static boolean isRemoteField(String field) {
        String[] split = field.split(DELIMITER, 2);
        return split.length == 2 && !split[0].contains("[");
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

    public static String getDelimiter(String groupFields, String currentField) {
        int index = groupFields.lastIndexOf(currentField);
        String delimiter = String.valueOf(groupFields.charAt(index + currentField.length()));
        if (delimiter.equals(GROUP_AND) || delimiter.equals(GROUP_OR)) {
            return delimiter;
        }
        return null;
    }
}
