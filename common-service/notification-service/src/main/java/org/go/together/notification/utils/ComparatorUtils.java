package org.go.together.notification.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.SimpleDto;
import org.go.together.exceptions.IncorrectDtoException;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.ComparingField;
import org.go.together.interfaces.Dto;
import org.go.together.interfaces.NamedEnum;
import org.go.together.notification.dto.ComparingObject;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComparatorUtils {
    private static final String CHANGED = "changed";
    private static final String FROM = StringUtils.SPACE + CHANGED + ": ";
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

    public static void compareObject(Collection<String> result,
                                     String fieldName,
                                     Object originalObject,
                                     Object changedObject) {
        if (originalObject != changedObject) {
            String resultString = fieldName + StringUtils.SPACE + CHANGED;
            result.add(resultString);
        }
    }

    public static void compareStrings(Collection<String> result, String fieldName, String originalString, String changedString) {
        if (!originalString.equalsIgnoreCase(changedString)) {
            String resultString = fieldName + FROM + originalString + TO + changedString;
            result.add(resultString);
        }
    }

    public static void compareNumbers(Collection<String> result, String fieldName, Number originalNumber, Number changedNumber) {
        if (!originalNumber.equals(changedNumber)) {
            String resultString = fieldName + FROM + originalNumber.toString() + TO + changedNumber.toString();
            result.add(resultString);
        }
    }

    private static <T extends Dto & ComparableDto> void updateResult(Collection<String> result,
                                                                     String fieldName,
                                                                     Object originalObject,
                                                                     Object changedObject,
                                                                     boolean isNeededDeepCompare,
                                                                     boolean ignored,
                                                                     boolean idCompare) {
        if (!isNeededDeepCompare || ignored) {
            compareObject(result, fieldName, originalObject, changedObject);
        } else if (originalObject instanceof String && changedObject instanceof String) {
            compareStrings(result, fieldName, (String) originalObject, (String) changedObject);
        } else if (originalObject instanceof Number && changedObject instanceof Number) {
            compareNumbers(result, fieldName, (Number) originalObject, (Number) changedObject);
        } else if (originalObject instanceof Date && changedObject instanceof Date) {
            compareDates(result, fieldName, (Date) originalObject, (Date) changedObject);
        } else if (originalObject instanceof NamedEnum && changedObject instanceof NamedEnum) {
            compareEnums(result, fieldName, (NamedEnum) originalObject, (NamedEnum) changedObject);
        } else if (originalObject instanceof SimpleDto && changedObject instanceof SimpleDto) {
            compareStrings(result, fieldName + " name", ((SimpleDto) originalObject).getName(), ((SimpleDto) changedObject).getName());
        } else if (originalObject instanceof Collection && changedObject instanceof Collection) {
            Collection<T> collection = (Collection) originalObject;
            Collection<T> anotherCollection = (Collection) changedObject;

            Iterator<T> iteratorCollection = collection.iterator();
            Iterator<T> iteratorAnotherCollection = anotherCollection.iterator();

            boolean isNotEmptyCollections = iteratorCollection.hasNext() || iteratorAnotherCollection.hasNext();
            if (isNotEmptyCollections) {
                if ((!iteratorCollection.hasNext() || iteratorCollection.next() != null) &&
                        (!iteratorAnotherCollection.hasNext() || iteratorAnotherCollection.next() != null)) {
                    compareCollectionDtos(result, fieldName, collection, anotherCollection, idCompare);
                }
            }
        } else if (originalObject instanceof Dto && changedObject instanceof Dto) {
            Dto comparableDtoObject = (Dto) originalObject;
            Dto anotherComparableDtoObject = (Dto) changedObject;

            if (idCompare) {
                compareObject(result, fieldName, comparableDtoObject.getId(), anotherComparableDtoObject.getId());
            } else {
                compareDtos(result, fieldName, comparableDtoObject, anotherComparableDtoObject);
            }
        } else if (originalObject != null && changedObject != null) {
            throw new IncorrectDtoException("Incorrect field type: " + fieldName);
        }
    }

    public static void compareDtos(Collection<String> result, String fieldName, Dto originalDto, Dto changedDto) {
        Set<String> strings = new HashSet<>();
        String mainField = getMainField(changedDto);
        getComparingMap(originalDto).forEach((key, value) -> {
            ComparingObject comparingField = getComparingMap(changedDto).get(key);
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
                    .append(StringUtils.SPACE)
                    .append(fieldName);
            if (StringUtils.isNotBlank(mainField)) {
                resultString.append(mainField);
            }
            resultString.append(": ")
                    .append(StringUtils.join(strings, ", "));
            result.add(resultString.toString());
        }
    }

    public static void compareDates(Collection<String> result, String fieldName, Date originalDate, Date changedDate) {
        if (originalDate.compareTo(changedDate) != 0) {
            String resultString = fieldName + FROM + originalDate.toString() + TO + changedDate.toString();
            result.add(resultString);
        }
    }

    public static void compareEnums(Collection<String> result, String fieldName, NamedEnum originalEnum, NamedEnum changedEnum) {
        if (originalEnum != changedEnum) {
            String resultString = fieldName + FROM + originalEnum.getDescription() + TO + changedEnum.getDescription();
            result.add(resultString);
        }
    }

    public static <T extends Dto> void compareCollectionDtos(Collection<String> result,
                                                             String fieldName,
                                                             Collection<T> originalDtos,
                                                             Collection<T> changedDtos,
                                                             boolean idCompare) {
        StringBuilder resultString = new StringBuilder();
        Map<UUID, ? extends List<T>> collectionIdsMap = originalDtos.stream()
                .collect(Collectors.groupingBy(Dto::getId));

        Map<UUID, ? extends List<T>> anotherCollectionIdsMap = changedDtos.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.groupingBy(Dto::getId));

        List<String> removedElements = new ArrayList<>();
        List<String> addedElements = changedDtos.stream()
                .filter(dto -> dto.getId() == null)
                .map(ComparatorUtils::getMainField)
                .collect(Collectors.toList());

        Set<String> changedElements = new HashSet<>();

        anotherCollectionIdsMap.forEach((key, value) -> {
            Dto anotherComparableDto = value.iterator().next();
            if (!collectionIdsMap.containsKey(key)) {
                addedElements.add(getMainField(anotherComparableDto));
            } else if (!idCompare) {
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
            resultString.append(CHANGED).append(StringUtils.SPACE).append(fieldName).append(" (");
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
