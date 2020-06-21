package org.go.together.utils;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ComparingObject;
import org.go.together.dto.SimpleDto;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.NamedEnum;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.join;
import static org.apache.commons.lang3.StringUtils.joinWith;

public class ComparatorUtils {
    private static final String CHANGED = "changed";
    private static final String FROM = " " + CHANGED + ": ";
    private static final String TO = " -> ";

    public static void compareObject(Collection<String> result, String fieldName, Object object, Object anotherObject) {
        if (!object.equals(anotherObject)) {
            String resultString = fieldName + " " + CHANGED;
            result.add(resultString);
        }
    }

    public static void compareStrings(Collection<String> result, String fieldName, String string, String anotherString) {
        if (!string.equals(anotherString)) {
            String resultString = fieldName + FROM + string + TO + anotherString;
            result.add(resultString);
        }
    }

    public static void compareNumbers(Collection<String> result, String fieldName, Number number, Number anotherNumber) {
        if (!number.equals(anotherNumber)) {
            String resultString = fieldName + FROM + number.toString() + TO + anotherNumber.toString();
            result.add(resultString);
        }
    }

    private static void updateResult(Collection<String> result, String fieldName, Object object, Object anotherObject,
                                     boolean isNeededDeepCompare) {
        if (!isNeededDeepCompare) {
            compareObject(result, fieldName, object, anotherObject);
        } else if (object instanceof String) {
            compareStrings(result, fieldName, (String) object, (String) anotherObject);
        } else if (object instanceof Number) {
            compareNumbers(result, fieldName, (Number) object, (Number) anotherObject);
        } else if (object instanceof Date) {
            compareDates(result, fieldName, (Date) object, (Date) anotherObject);
        } else if (object instanceof NamedEnum) {
            compareEnums(result, fieldName, (NamedEnum) object, (NamedEnum) anotherObject);
        } else if (object instanceof SimpleDto) {
            compareStrings(result, fieldName + " name", ((SimpleDto) object).getName(), ((SimpleDto) anotherObject).getName());
        } else if (object instanceof Collection) {
            Collection collection = (Collection) object;
            Collection anotherCollection = (Collection) anotherObject;

            boolean isNotEmptyCollections = collection.iterator().hasNext() || anotherCollection.iterator().hasNext();
            if (isNotEmptyCollections) {
                if ((!collection.iterator().hasNext() || collection.iterator().next() instanceof Dto) &&
                        (!anotherCollection.iterator().hasNext() || collection.iterator().next() instanceof Dto)) {
                    compareCollectionDtos(result, fieldName, collection, anotherCollection);
                }
            }
        } else if (object instanceof Dto) {
            compareDtos(result, fieldName, (Dto) object, (Dto) anotherObject);
        } else {
            throw new IncorrectDtoException("Incorrect field type: " + fieldName);
        }
    }

    public static void compareDtos(Collection<String> result, String fieldName, Dto dto, Dto anotherDto) {
        Set<String> strings = new HashSet<>();
        StringBuilder mainField = new StringBuilder();
        dto.getComparingMap().forEach((key, value) -> {
            if (value.getIsMain()) {
                mainField.append(value.getGetDtoField().get().toString());
            }
            ComparingObject comparingField = anotherDto.getComparingMap().get(key);
            updateResult(strings, key, value.getGetDtoField().get(),
                    comparingField.getGetDtoField().get(),
                    value.getIsNeededDeepCompare());
        });
        if (!strings.isEmpty()) {
            StringBuilder resultString = new StringBuilder()
                    .append(CHANGED.replaceFirst("c", "C"))
                    .append(" ")
                    .append(fieldName);
            if (StringUtils.isNotBlank(mainField)) {
                resultString
                        .append(" ")
                        .append("\"")
                        .append(mainField)
                        .append("\"");
            }
            resultString.append(": ")
                    .append(join(strings, ", "));
            result.add(resultString.toString());
        }
    }

    public static void compareDates(Collection<String> result, String fieldName, Date date, Date anotherDate) {
        if (!date.equals(anotherDate)) {
            String resultString = fieldName + FROM + date.toString() + TO + anotherDate.toString();
            result.add(resultString);
        }
    }

    public static void compareEnums(Collection<String> result, String fieldName, NamedEnum oneEnum, NamedEnum anotherEnum) {
        if (oneEnum != anotherEnum) {
            String resultString = fieldName + FROM + oneEnum.getDescription() + TO + anotherEnum.getDescription();
            result.add(resultString);
        }
    }

    public static void compareCollectionDtos(Collection<String> result, String fieldName,
                                             Collection<? extends Dto> collectionDtos, Collection<? extends Dto> anotherCollectionDtos) {
        StringBuilder resultString = new StringBuilder();
        Map<UUID, ? extends List<? extends Dto>> collectionIdsMap = collectionDtos.stream()
                .collect(Collectors.groupingBy(Dto::getId));

        Map<UUID, ? extends List<? extends Dto>> anotherCollectionIdsMap = anotherCollectionDtos.stream()
                .collect(Collectors.groupingBy(Dto::getId));

        Set<String> removedElements = new HashSet<>();
        Set<String> addedElements = new HashSet<>();
        Set<String> changedElements = new HashSet<>();

        anotherCollectionIdsMap.forEach((key, value) -> {
            Dto anotherDto = value.iterator().next();
            if (!collectionIdsMap.containsKey(key)) {
                addedElements.add(getMainField(anotherDto));
            } else {
                Set<String> resultComparing = new HashSet<>();
                Dto dto = collectionIdsMap.get(key).iterator().next();
                compareDtos(resultComparing, fieldName, dto, anotherDto);
                if (!resultComparing.isEmpty()) {
                    changedElements.add(joinWith(",", resultComparing));
                }
            }
        });

        collectionIdsMap.keySet().forEach(key -> {
            if (!anotherCollectionIdsMap.containsKey(key)) {
                Dto dto = collectionIdsMap.get(key).iterator().next();
                removedElements.add(getMainField(dto));
            }
        });

        if (!removedElements.isEmpty() || !addedElements.isEmpty() || !changedElements.isEmpty()) {
            resultString.append(CHANGED).append(" ").append(fieldName).append(" (");
            StringBuilder removedElementsString = new StringBuilder();
            if (!removedElements.isEmpty()) {
                removedElementsString
                        .append(" removed ")
                        .append(join(removedElements, ", "));
            }
            StringBuilder addedElementsString = new StringBuilder();
            if (!addedElements.isEmpty()) {
                addedElementsString
                        .append(" added ")
                        .append(join(addedElements, ", "));
            }
            StringBuilder changedElementsString = new StringBuilder();
            if (!changedElements.isEmpty()) {
                addedElementsString
                        .append(" changed ")
                        .append(join(changedElements, ", "));
            }
            String resultElementsString = Stream.of(removedElementsString, addedElementsString, changedElementsString)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining("and"));
            resultString.append(resultElementsString);
            resultString.append(") ");
            result.add(resultString.toString());
        }
    }

    private static String getMainField(Dto dto) {
        return dto.getComparingMap().entrySet().stream()
                .filter(entry -> entry.getValue().getIsMain())
                .map(Map.Entry::getKey)
                .map(key -> "\"" + key + "\"")
                .findFirst().orElse(StringUtils.EMPTY);
    }
}
