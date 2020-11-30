package org.go.together.find.utils;

import org.go.together.dto.FilterDto;
import org.go.together.enums.FindOperator;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.find.dto.FieldDto;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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

    public static Map<FieldDto, FilterDto> mergeFilters(Map<FieldDto, Collection<Object>> remoteFilters,
                                                        Map<FieldDto, FilterDto> localFilters) {
        boolean isNotFound = remoteFilters.values().stream().anyMatch(Collection::isEmpty);
        if (isNotFound) {
            return Collections.emptyMap();
        }
        remoteFilters.forEach((key, values) -> {
            FilterDto filterDto = new FilterDto(FindOperator.IN,
                    Collections.singleton(Collections.singletonMap(getCorrectFilterValuesKey(key), values)));
            localFilters.remove(key);
            localFilters.put(key, filterDto);
        });

        return localFilters;
    }

    private static String getCorrectFilterValuesKey(FieldDto fieldDto) {
        return Optional.ofNullable(fieldDto.getLocalField())
                .map(FindUtils::getParsedFields)
                .map(splitByDotString -> splitByDotString[splitByDotString.length - 1])
                .orElseThrow(() -> new IncorrectDtoException("Incorrect search field"));
    }
}
