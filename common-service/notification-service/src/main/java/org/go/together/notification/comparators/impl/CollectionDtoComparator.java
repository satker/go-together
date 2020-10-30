package org.go.together.notification.comparators.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.dto.ComparingObject;
import org.go.together.interfaces.ComparableDto;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class CollectionDtoComparator implements Comparator<Collection<ComparableDto>> {
    private Comparator<ComparableDto> comparableDtoComparator;

    @Autowired
    public void setComparableDtoComparator(Comparator<ComparableDto> comparableDtoComparator) {
        this.comparableDtoComparator = comparableDtoComparator;
    }

    @Override
    public Map<String, Object> compare(String fieldName, Collection<ComparableDto> originalObject,
                                       Collection<ComparableDto> changedObject, ComparingObject fieldProperties) {
        originalObject = Optional.ofNullable(originalObject).orElse(Collections.emptySet());
        changedObject = Optional.ofNullable(changedObject).orElse(Collections.emptySet());

        Map<UUID, ComparableDto> originalIdsMap = originalObject.stream()
                .collect(Collectors.toMap(ComparableDto::getId, Function.identity()));

        Map<UUID, ComparableDto> changedIdsMap = changedObject.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(ComparableDto::getId, Function.identity()));

        Collection<String> addedElements = findAddedElements(changedObject, originalIdsMap, changedIdsMap);
        Map<String, Object> changedElements = findChangedElements(fieldProperties, originalIdsMap, changedIdsMap);
        Collection<String> removedElements = findRemovedElements(originalIdsMap, changedIdsMap);

        Map<String, Object> resultMap = getComparingMap(removedElements, addedElements, changedElements);

        return Map.of(fieldName, resultMap);
    }

    private Map<String, Object> findChangedElements(ComparingObject fieldProperties,
                                                    Map<UUID, ComparableDto> originalIdsMap,
                                                    Map<UUID, ComparableDto> changedIdsMap) {
        if (fieldProperties.getIdCompare()) {
            return Collections.emptyMap();
        } else {
            Map<String, Object> resultMap = new HashMap<>();
            changedIdsMap.entrySet().stream()
                    .map(entry -> Optional.ofNullable(originalIdsMap.get(entry.getKey()))
                            .map(dto -> comparableDtoComparator.compare(ITEM, dto, entry.getValue(), fieldProperties))
                            .orElse(Collections.emptyMap()))
                    .filter(map -> !map.isEmpty())
                    .forEach(resultMap::putAll);
            return resultMap;
        }
    }

    private Collection<String> findAddedElements(Collection<ComparableDto> changedObject,
                                                 Map<UUID, ComparableDto> originalIdsMap,
                                                 Map<UUID, ComparableDto> changedIdsMap) {
        Collection<String> addedElements = changedObject.stream()
                .filter(dto -> dto.getId() == null)
                .map(ComparableDto::getMainField)
                .collect(Collectors.toList());
        changedIdsMap.entrySet().stream()
                .filter(changedId -> !originalIdsMap.containsKey(changedId.getKey()))
                .map(Map.Entry::getValue)
                .map(ComparableDto::getMainField)
                .forEach(addedElements::add);
        return addedElements;
    }

    private Collection<String> findRemovedElements(Map<UUID, ComparableDto> originalIdsMap, Map<UUID, ComparableDto> changedIdsMap) {
        return originalIdsMap.keySet().stream()
                .filter(originalId -> !changedIdsMap.containsKey(originalId))
                .map(originalIdsMap::get)
                .map(ComparableDto::getMainField)
                .collect(Collectors.toList());
    }

    private Map<String, Object> getComparingMap(Collection<String> removedElements,
                                                Collection<String> addedElements,
                                                Map<String, Object> changedElements) {
        if (!removedElements.isEmpty() || !addedElements.isEmpty() || !changedElements.isEmpty()) {
            Map<String, Object> removedElementsString = produceAddedElementsResult(removedElements, REMOVED);
            Map<String, Object> addedElementsString = produceAddedElementsResult(addedElements, ADDED);

            changedElements.putAll(removedElementsString);
            changedElements.putAll(addedElementsString);

            return changedElements;
        }
        return Collections.emptyMap();
    }

    private Map<String, Object> produceAddedElementsResult(Collection<String> elements, String action) {
        if (!elements.isEmpty()) {
            String message = StringUtils.isBlank(elements.iterator().next()) ?
                    elements.size() + ITEMS :
                    StringUtils.join(elements, COMMA);
            return Map.of(action, message);
        }
        return Collections.emptyMap();
    }
}
