package org.go.together.utils;

import org.go.together.dto.SimpleDto;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.NamedEnum;

import java.util.*;
import java.util.stream.Collectors;

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
        dto.getComparingMap().forEach((key, value) -> updateResult(strings, key, value.getGetDtoField().get(),
                anotherDto.getComparingMap().get(key).getGetDtoField().get(),
                value.getIsNeededDeepCompare()));
        if (!strings.isEmpty()) {
            String resultString = CHANGED + " " + fieldName + ": " + joinWith(". ", strings);
            result.add(resultString);
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
        Set<UUID> collectionIds = collectionDtos.stream().map(Dto::getId).collect(Collectors.toSet());
        Set<UUID> anotherCollectionIds = anotherCollectionDtos.stream().map(Dto::getId).collect(Collectors.toSet());
        if (collectionIds.size() > anotherCollectionIds.size()) {
            resultString.append("removed ")
                    .append(collectionIds.size() - anotherCollectionIds.size())
                    .append(" ")
                    .append(fieldName);
            result.add(resultString.toString());
        } else if (collectionIds.size() < anotherCollectionIds.size()) {
            resultString.append("added ")
                    .append(anotherCollectionIds.size() - collectionIds.size())
                    .append(" ")
                    .append(fieldName);
            result.add(resultString.toString());
        } else {
            if (anotherCollectionDtos.stream()
                    .map(Dto::getId)
                    .anyMatch(Objects::isNull)) {
                resultString.append(CHANGED).append(" ").append(fieldName);
                result.add(resultString.toString());
            }
        }
    }
}
