package org.go.together.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ComparingObject;
import org.go.together.dto.SimpleDto;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.NamedEnum;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComparatorUtils {
    private static final String CHANGED = "changed";
    private static final String FROM = " " + CHANGED + ": ";
    private static final String TO = " -> ";

    public static String getMainField(Dto dto) {
        return Optional.ofNullable(dto)
                .map(ComparatorUtils::getComparingMap)
                .map(Map::values)
                .orElse(Collections.emptySet())
                .stream()
                .filter(ComparingObject::getIsMain)
                .findFirst()
                .map(ComparingObject::getFieldValue)
                .map(Object::toString)
                .map(string -> " '" + string + "'")
                .orElse(StringUtils.EMPTY);
    }

    public static void compareObject(Collection<String> result, String fieldName, Object object, Object anotherObject) {
        if (object != anotherObject) {
            String resultString = fieldName + " " + CHANGED;
            result.add(resultString);
        }
    }

    public static void compareStrings(Collection<String> result, String fieldName, String string, String anotherString) {
        if (!string.equalsIgnoreCase(anotherString)) {
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

    private static <T extends Dto & ComparableDto> void updateResult(Collection<String> result, String fieldName, Object object, Object anotherObject,
                                                                     boolean isNeededDeepCompare, boolean ignored, boolean idCompare) {
        if (!isNeededDeepCompare || ignored) {
            compareObject(result, fieldName, object, anotherObject);
        } else if (object instanceof String && anotherObject instanceof String) {
            compareStrings(result, fieldName, (String) object, (String) anotherObject);
        } else if (object instanceof Number && anotherObject instanceof Number) {
            compareNumbers(result, fieldName, (Number) object, (Number) anotherObject);
        } else if (object instanceof Date && anotherObject instanceof Date) {
            compareDates(result, fieldName, (Date) object, (Date) anotherObject);
        } else if (object instanceof NamedEnum && anotherObject instanceof NamedEnum) {
            compareEnums(result, fieldName, (NamedEnum) object, (NamedEnum) anotherObject);
        } else if (object instanceof SimpleDto && anotherObject instanceof SimpleDto) {
            compareStrings(result, fieldName + " name", ((SimpleDto) object).getName(), ((SimpleDto) anotherObject).getName());
        } else if (object instanceof Collection && anotherObject instanceof Collection) {
            Collection<T> collection = (Collection) object;
            Collection<T> anotherCollection = (Collection) anotherObject;

            Iterator<T> iteratorCollection = collection.iterator();
            Iterator<T> iteratorAnotherCollection = anotherCollection.iterator();

            boolean isNotEmptyCollections = iteratorCollection.hasNext() || iteratorAnotherCollection.hasNext();
            if (isNotEmptyCollections) {
                if ((!iteratorCollection.hasNext() || iteratorCollection.next() != null) &&
                        (!iteratorAnotherCollection.hasNext() || iteratorAnotherCollection.next() != null)) {
                    compareCollectionDtos(result, fieldName, collection, anotherCollection);
                }
            }
        } else if (object instanceof Dto && anotherObject instanceof Dto) {
            Dto comparableDtoObject = (Dto) object;
            Dto anotherComparableDtoObject = (Dto) anotherObject;

            if (idCompare) {
                compareObject(result, fieldName, comparableDtoObject.getId(), anotherComparableDtoObject.getId());
            } else {
                compareDtos(result, fieldName, comparableDtoObject, anotherComparableDtoObject);
            }
        } else if (object != null && anotherObject != null) {
            throw new IncorrectDtoException("Incorrect field type: " + fieldName);
        }
    }

    public static void compareDtos(Collection<String> result, String fieldName, Dto comparableDto, Dto anotherComparableDto) {
        Set<String> strings = new HashSet<>();
        String mainField = getMainField(anotherComparableDto);
        getComparingMap(comparableDto).forEach((key, value) -> {
            ComparingObject comparingField = getComparingMap(anotherComparableDto).get(key);
            if (comparingField.getFieldValue() != null || value.getFieldValue() != null) {
                updateResult(strings, key, value.getFieldValue(),
                        comparingField.getFieldValue(),
                        value.getIsDeepCompare(),
                        value.getIgnored(),
                        value.getIdCompare());
            }
        });
        if (!strings.isEmpty()) {
            StringBuilder resultString = new StringBuilder()
                    .append(CHANGED.replaceFirst("c", "C"))
                    .append(" ")
                    .append(fieldName);
            if (StringUtils.isNotBlank(mainField)) {
                resultString.append(mainField);
            }
            resultString.append(": ")
                    .append(StringUtils.join(strings, ", "));
            result.add(resultString.toString());
        }
    }

    public static void compareDates(Collection<String> result, String fieldName, Date date, Date anotherDate) {
        if (date.compareTo(anotherDate) != 0) {
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

    public static <T extends Dto> void compareCollectionDtos(Collection<String> result, String fieldName,
                                                             Collection<T> collectionDtos,
                                                             Collection<T> anotherCollectionDtos) {
        StringBuilder resultString = new StringBuilder();
        Map<UUID, ? extends List<T>> collectionIdsMap = collectionDtos.stream()
                .collect(Collectors.groupingBy(Dto::getId));

        Map<UUID, ? extends List<T>> anotherCollectionIdsMap = anotherCollectionDtos.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.groupingBy(Dto::getId));

        List<String> removedElements = new ArrayList<>();
        List<String> addedElements = anotherCollectionDtos.stream()
                .filter(dto -> dto.getId() == null)
                .map(ComparatorUtils::getMainField)
                .collect(Collectors.toList());

        Set<String> changedElements = new HashSet<>();

        anotherCollectionIdsMap.forEach((key, value) -> {
            Dto anotherComparableDto = value.iterator().next();
            if (!collectionIdsMap.containsKey(key)) {
                addedElements.add(getMainField(anotherComparableDto));
            } else {
                Set<String> resultComparing = new HashSet<>();
                Dto comparableDto = collectionIdsMap.get(key).iterator().next();
                compareDtos(resultComparing, fieldName, comparableDto, anotherComparableDto);
                if (!resultComparing.isEmpty()) {
                    changedElements.add(StringUtils.join(resultComparing, ","));
                }
            }
        });

        collectionIdsMap.keySet().forEach(key -> {
            if (!anotherCollectionIdsMap.containsKey(key)) {
                Dto comparableDto = collectionIdsMap.get(key).iterator().next();
                removedElements.add(getMainField(comparableDto));
            }
        });

        if (!removedElements.isEmpty() || !addedElements.isEmpty() || !changedElements.isEmpty()) {
            resultString.append(CHANGED).append(" ").append(fieldName).append(" (");
            StringBuilder removedElementsString = new StringBuilder();
            if (!removedElements.isEmpty()) {
                String message = StringUtils.isBlank(removedElements.iterator().next()) ?
                        removedElements.size() + " elements " :
                        StringUtils.join(removedElements, ", ");
                removedElementsString
                        .append(" removed ")
                        .append(message);
            }
            StringBuilder addedElementsString = new StringBuilder();
            if (!addedElements.isEmpty()) {
                String message = StringUtils.isBlank(addedElements.iterator().next()) ?
                        addedElements.size() + " elements " :
                        StringUtils.join(addedElements, ", ");
                addedElementsString
                        .append(" added ")
                        .append(message);
            }
            StringBuilder changedElementsString = new StringBuilder();
            if (!changedElements.isEmpty()) {
                addedElementsString.append(StringUtils.join(changedElements, ", "));
            }
            String resultElementsString = Stream.of(removedElementsString, addedElementsString, changedElementsString)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(" and"));
            resultString.append(resultElementsString);
            resultString.append(") ");
            result.add(resultString.toString());
        }
    }

    private static <T extends Dto> Map<String, ComparingObject> getComparingMap(T dto) {
        HashMap<String, ComparingObject> result = new HashMap<>();
        Stream.of(dto.getClass().getDeclaredFields())
                .forEach(field -> {
                    ComparingField annotation = field.getAnnotation(ComparingField.class);
                    if (annotation != null) {
                        String name = field.getName();
                        String methodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                        try {
                            Method fieldGetter = dto.getClass().getDeclaredMethod(methodName);
                            Object fieldValue = fieldGetter.invoke(dto);
                            boolean ignored = field.isAnnotationPresent(JsonIgnore.class);
                            ComparingObject comparingObject =
                                    new ComparingObject(fieldValue, annotation.deepCompare(), annotation.isMain(), ignored,
                                            annotation.idCompare());
                            result.put(annotation.value(), comparingObject);
                        } catch (Exception e) {
                            throw new IncorrectDtoException("Cannot find field '" + name + "' getter");
                        }
                    }
                });
        return result;
    }
}
