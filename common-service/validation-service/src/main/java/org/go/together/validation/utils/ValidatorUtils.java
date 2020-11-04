package org.go.together.validation.utils;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.FindClient;
import org.go.together.dto.SimpleDto;
import org.go.together.interfaces.Dto;
import org.go.together.validation.dto.DateIntervalDto;
import org.go.together.validation.dto.NumberIntervalDto;
import org.go.together.validation.dto.StringRegexDto;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ValidatorUtils {
    public static <D extends Dto> String checkNullObject(Map<String, Function<D, Object>> objectMap, D dto) {
        final String message = "Field %s is null. ";
        return objectMap.entrySet().stream()
                .map(string -> Objects.isNull(string.getValue().apply(dto)) ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static <D extends Dto> String checkEmptyString(Map<String, Function<D, String>> stringsFunction, D dto) {
        final String message = "Field %s is empty or null. ";
        return stringsFunction.entrySet().stream()
                .map(string -> StringUtils.isBlank(string.getValue().apply(dto)) ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());

    }

    public static <D extends Dto> String checkZeroOrNegativeNumber(Map<String, Function<D, Number>> numbersForCheck, D dto) {
        final String message = "Number %s is zero or negative ";
        return numbersForCheck.entrySet().stream()
                .map(number -> number.getValue().apply(dto).doubleValue() <= 0 ?
                        String.format(message, number.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static <D extends Dto> String checkEmptySimpleDto(Map<String, Function<D, SimpleDto>> simpleDtoMap,
                                                             D dto) {
        final String message = "Field %s is incorrect. ";
        return simpleDtoMap.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().apply(dto)))
                .map(string -> string.getValue() == null || isCorrectSimpleDto(string.getValue()) ?
                        String.format(message, string.getKey()) :
                        StringUtils.EMPTY)
                .collect(Collectors.joining());
    }

    public static <D extends Dto> String checkCorrectCollection(Map<String, Function<D, Collection<?>>> collectionMap,
                                                                D dto) {
        final String message = "Collection %s is incorrect. ";
        return collectionMap.entrySet().stream()
                .map(entry -> Map.entry(entry.getKey(), entry.getValue().apply(dto)))
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
        return dateIntervalMap.entrySet().stream()
                .map(ValidatorUtils::checkDates)
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

    private static boolean isCorrectSimpleDto(SimpleDto simpleDto) {
        return simpleDto == null || simpleDto.getId() == null || StringUtils.isBlank(simpleDto.getName());
    }

    private static String checkDates(Map.Entry<String, DateIntervalDto> dateIntervalDto) {
        DateIntervalDto value = dateIntervalDto.getValue();
        if (value.getStartDate() == null || value.getEndDate() == null) {
            return "Dates is not entered. ";
        }

        Date currentDate = new Date();
        if ((!(currentDate.before(value.getStartDate()) &&
                currentDate.before(value.getEndDate()))) ||
                value.getStartDate().after(value.getEndDate())) {
            return String.format("Number interval for %s is incorrect. ", dateIntervalDto.getKey());
        } else {
            return StringUtils.EMPTY;
        }
    }

    public static <D extends Dto> String checkAnotherServiceDtoIsCorrect(Map<FindClient, D> anotherServiceCorrectCheck) {
        return anotherServiceCorrectCheck.entrySet().stream()
                .map(entry -> entry.getKey().validate(entry.getValue()))
                .collect(Collectors.joining());
    }
}
