package org.go.together.notification.comparators.impl.collection.finders.impl;

import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingObject;
import org.go.together.notification.comparators.impl.collection.finders.interfaces.Finder;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.go.together.notification.comparators.interfaces.Comparator.CHANGED;

@Component
public class DtoFinder<T extends ComparableDto> implements Finder<T> {
    private Comparator<T> comparableDtoComparator;

    @Autowired
    public void setComparableDtoComparator(Comparator<T> comparableDtoComparator) {
        this.comparableDtoComparator = comparableDtoComparator;
    }

    @Override
    public Map<String, Object> findChanged(Collection<T> changedObject,
                                           Collection<T> originalObject,
                                           ComparingObject fieldProperties) {
        if (fieldProperties.getIdCompare()) {
            return Collections.emptyMap();
        } else {
            Map<String, Object> resultMap = new HashMap<>();
            HashMap<String, Object> changedValues = new HashMap<>();
            Map<UUID, T> uuidOriginalMap = originalObject.stream()
                    .collect(Collectors.toMap(ComparableDto::getId, Function.identity()));
            changedObject.stream()
                    .map(comparableDto -> Optional.ofNullable(uuidOriginalMap.get(comparableDto.getId()))
                            .map(dto -> comparableDtoComparator.compare(dto.getMainField(), dto, comparableDto))
                            .orElse(Collections.emptyMap()))
                    .filter(map -> !map.isEmpty())
                    .forEach(changedValues::putAll);
            resultMap.put(CHANGED, changedValues);
            return resultMap;
        }
    }

    @Override
    public Collection<String> findAdded(Collection<T> changedObject,
                                        Collection<T> originalObject) {
        Map<UUID, T> originalIdsMap = originalObject.stream()
                .collect(Collectors.toMap(ComparableDto::getId, Function.identity()));

        Map<UUID, T> changedIdsMap = changedObject.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(ComparableDto::getId, Function.identity()));

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

    @Override
    public Collection<String> findRemoved(Collection<T> changedObject,
                                          Collection<T> originalObject) {
        Map<UUID, T> originalIdsMap = originalObject.stream()
                .collect(Collectors.toMap(ComparableDto::getId, Function.identity()));

        Map<UUID, T> changedIdsMap = changedObject.stream()
                .filter(dto -> dto.getId() != null)
                .collect(Collectors.toMap(ComparableDto::getId, Function.identity()));

        return originalIdsMap.keySet().stream()
                .filter(originalId -> !changedIdsMap.containsKey(originalId))
                .map(originalIdsMap::get)
                .map(ComparableDto::getMainField)
                .collect(Collectors.toList());
    }
}
