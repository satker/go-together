package org.go.together.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.validation.DateIntervalDto;
import org.go.together.dto.validation.NumberIntervalDto;
import org.go.together.dto.validation.StringRegexDto;
import org.go.together.interfaces.Dto;

import java.util.*;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidatorUtils {
    public static String checkNullObject(Map<String, Object> objectMap) {
        final String message = "Field %s is null. ";
        return objectMap.entrySet().stream()
                .map(string -> string.getValue() == null ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static String checkEmptyString(Map<String, String> stringsForCheck) {
        final String message = "Field %s is empty or null. ";
        return stringsForCheck.entrySet().stream()
                .map(string -> StringUtils.isBlank(string.getValue()) ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());

    }

    public static String checkZeroOrNegativeNumber(Map<String, Number> numbersForCheck) {
        final String message = "Number %s is zero or negative ";
        return numbersForCheck.entrySet().stream()
                .map(number -> number.getValue().doubleValue() <= 0 ?
                        String.format(message, number.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static String checkNegativeNumber(Map<String, Number> numbersForCheck) {
        final String message = "Number %s is negative ";
        return numbersForCheck.entrySet().stream()
                .map(number -> number.getValue().doubleValue() < 0 ?
                        String.format(message, number.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static String checkEmptySimpleDto(Map<String, Collection<SimpleDto>> simpleDtoMap) {
        final String message = "Field %s is incorrect. ";
        return simpleDtoMap.entrySet().stream()
                .map(string -> string.getValue() == null || isCorrectSimpleDto(string.getValue()) ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static String checkCorrectCollection(Map<String, Collection<?>> collectionMap) {
        final String message = "Collection %s is incorrect. ";
        return collectionMap.entrySet().stream()
                .map(string -> {
                    boolean b = string.getValue() == null || string.getValue().isEmpty() ||
                            string.getValue().stream().anyMatch(Objects::isNull);
                    return b ?
                            String.format(message, string.getKey()) :
                            StringUtils.EMPTY;
                })
                .collect(Collectors.joining());
    }

    public static String checkStringByRegex(Map<String, StringRegexDto> stringRegexMap) {
        final String message = "String %s is incorrect. ";
        return stringRegexMap.entrySet().stream()
                .map(string -> !Pattern.compile(string.getValue().getRegex())
                        .matcher(string.getValue().getString()).matches() ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static String checkDateIntervalIsCorrect(Map<String, DateIntervalDto> dateIntervalMap) {
        final String message = "Date interval for %s is incorrect. ";
        Date currentDate = new Date();
        return dateIntervalMap.entrySet().stream()
                .map(string -> (!(currentDate.before(string.getValue().getStartDate()) &&
                        currentDate.before(string.getValue().getEndDate()))) ||
                        string.getValue().getStartDate().after(string.getValue().getEndDate()) ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static String checkNumberIntervalIsCorrect(Map<String, NumberIntervalDto> numberIntervalDtoMap) {
        final String message = "Number interval for %s is incorrect. ";
        return numberIntervalDtoMap.entrySet().stream()
                .map(string -> string.getValue().getNumber().doubleValue() < string.getValue().getMin().doubleValue() ||
                        string.getValue().getNumber().doubleValue() > string.getValue().getMax().doubleValue() ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static String checkUUIDIsCorrect(Map<String, UUID> uuidMap) {
        final String message = "Id %s is incorrect. ";
        return uuidMap.entrySet().stream()
                .map(uuid -> uuid.getValue() == null ?
                        String.format(message, uuid.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static String checkAnotherServiceValidationCorrectCheck(Map<String, Pair<Function<Dto, String>,
            Collection<Dto>>> mapForCheck) {
        final String message = "Dto field %s don't pass validation: ";
        return mapForCheck.entrySet().stream()
                .map(stringPairEntry -> {
                    String errorsCollection = stringPairEntry.getValue().getValue().stream()
                            .map(stringPairEntry.getValue().getKey())
                            .filter(StringUtils::isNotBlank)
                            .collect(Collectors.joining(". "));
                    if (StringUtils.isNotBlank(errorsCollection)) {
                        return String.format(message, stringPairEntry.getKey()).concat(errorsCollection);
                    }
                    return StringUtils.EMPTY;
                })
                .collect(Collectors.joining());
    }

    public static boolean isUUIDCorrect(String uuid) {
        try {
            UUID.fromString(uuid);
            return StringUtils.isNotBlank(uuid);
        } catch (Exception e) {
            return false;
        }
    }


    private static boolean isCorrectSimpleDto(Collection<SimpleDto> simpleDto) {
        if (simpleDto.isEmpty()) {
            return false;
        }
        return simpleDto.stream().anyMatch(dto ->
                dto == null || StringUtils.isBlank(dto.getId()) || StringUtils.isBlank(dto.getName()));
    }
}
