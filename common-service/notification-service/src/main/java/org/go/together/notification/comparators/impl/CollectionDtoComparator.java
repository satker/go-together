package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.interfaces.ComparableDto;
import org.go.together.interfaces.Dto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class CollectionDtoComparator implements Comparator<Collection<ComparableDto>> {
    private Comparator<ComparableDto> comparableDtoComparator;

    @Autowired
    public void setComparableDtoComparator(Comparator<ComparableDto> comparableDtoComparator) {
        this.comparableDtoComparator = comparableDtoComparator;
    }

    @Override
    public String compare(String fieldName,
                          Collection<ComparableDto> originalObject, Collection<ComparableDto> changedObject, boolean idCompare) {
        StringBuilder resultString = new StringBuilder();
        Map<UUID, ? extends List<ComparableDto>> originalIdsMap = originalObject.stream()
                .collect(Collectors.groupingBy(ComparableDto::getId));

        Map<UUID, ? extends List<ComparableDto>> changedIdsMap = changedObject.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.groupingBy(Dto::getId));

        Set<String> addedElements = findAddedElements(changedObject, originalIdsMap, changedIdsMap);
        Set<String> changedElements = findChangedElements(fieldName, idCompare, originalIdsMap, changedIdsMap);
        Set<String> removedElements = findRemovedElements(originalIdsMap, changedIdsMap);

        String concatComparingResult = concatComparingResult(removedElements, addedElements, changedElements);

        return produceComparingResult(fieldName, resultString, concatComparingResult);
    }

    private Set<String> findChangedElements(String fieldName,
                                            boolean idCompare,
                                            Map<UUID, ? extends List<ComparableDto>> originalIdsMap,
                                            Map<UUID, ? extends List<ComparableDto>> changedIdsMap) {
        if (idCompare) {
            return Collections.emptySet();
        } else {
            return changedIdsMap.entrySet().stream()
                    .map(entry -> {
                        ComparableDto anotherComparableDto = entry.getValue().iterator().next();
                        Set<String> resultComparing = new HashSet<>();
                        ComparableDto comparableDto = originalIdsMap.get(entry.getKey()).iterator().next();
                        String comparingResult = comparableDtoComparator.compare(fieldName, comparableDto, anotherComparableDto, false);
                        resultComparing.add(comparingResult);
                        if (!resultComparing.isEmpty()) {
                            return StringUtils.join(resultComparing, ",");
                        }
                        return StringUtils.EMPTY;
                    }).collect(Collectors.toSet());
        }
    }

    private Set<String> findAddedElements(Collection<ComparableDto> changedObject,
                                          Map<UUID, ? extends List<ComparableDto>> originalIdsMap,
                                          Map<UUID, ? extends List<ComparableDto>> changedIdsMap) {
        Set<String> addedElements = changedObject.stream()
                .filter(dto -> dto.getId() == null)
                .map(ComparableDto::getMainField)
                .collect(Collectors.toSet());
        changedIdsMap.entrySet().stream()
                .filter(changedId -> !originalIdsMap.containsKey(changedId.getKey()))
                .map(Map.Entry::getValue)
                .map(List::iterator)
                .filter(Iterator::hasNext)
                .map(Iterator::next)
                .map(ComparableDto::getMainField)
                .forEach(addedElements::add);
        return addedElements;
    }

    private Set<String> findRemovedElements(Map<UUID, ? extends List<ComparableDto>> originalIdsMap, Map<UUID, ? extends List<ComparableDto>> changedIdsMap) {
        return originalIdsMap.keySet().stream()
                .filter(originalId -> !changedIdsMap.containsKey(originalId))
                .map(originalId -> originalIdsMap.get(originalId).iterator().next())
                .map(ComparableDto::getMainField)
                .collect(Collectors.toSet());
    }

    private String produceComparingResult(String fieldName,
                                          StringBuilder resultString,
                                          String concatComparingResult) {

        return resultString.append(CHANGED)
                .append(StringUtils.SPACE)
                .append(fieldName)
                .append(" (")
                .append(concatComparingResult)
                .append(") ").toString();
    }

    private String concatComparingResult(Set<String> removedElements,
                                         Set<String> addedElements,
                                         Set<String> changedElements) {
        if (!removedElements.isEmpty() || !addedElements.isEmpty() || !changedElements.isEmpty()) {
            String removedElementsString = produceAddedElementsResult(removedElements, " removed ");
            String addedElementsString = produceAddedElementsResult(addedElements, " added ");
            String changedElementsString = produceChangedElementsResult(changedElements, addedElementsString);

            return Stream.of(removedElementsString, addedElementsString, changedElementsString)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.joining(" and"));
        }
        return StringUtils.EMPTY;
    }

    private String produceChangedElementsResult(Set<String> changedElements,
                                                String addedElementsString) {
        if (!changedElements.isEmpty()) {
            return addedElementsString + StringUtils.join(changedElements, COMMA);
        }
        return StringUtils.EMPTY;
    }

    private String produceAddedElementsResult(Set<String> removedElements, String action) {
        if (!removedElements.isEmpty()) {
            String message = StringUtils.isBlank(removedElements.iterator().next()) ?
                    removedElements.size() + " elements " :
                    StringUtils.join(removedElements, COMMA);
            return action + message;
        }
        return StringUtils.EMPTY;
    }
}
