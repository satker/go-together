package org.go.together.notification.comparators.impl.collection;

import org.go.together.compare.ComparingObject;
import org.go.together.interfaces.ImplFinder;
import org.go.together.notification.comparators.impl.collection.finders.interfaces.Finder;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class CollectionComparator implements Comparator<Collection<?>> {
    private ImplFinder<Finder<?>> finderImplFinder;

    @Autowired
    public void setFinderTransformer(ImplFinder<Finder<?>> finderImplFinder) {
        this.finderImplFinder = finderImplFinder;
    }

    @Override
    public Map<String, Object> compare(String fieldName, Collection<?> originalObject,
                                       Collection<?> changedObject, ComparingObject fieldProperties) {
        originalObject = Optional.ofNullable(originalObject).orElse(Collections.emptySet());
        changedObject = Optional.ofNullable(changedObject).orElse(Collections.emptySet());

        Finder finder = finderImplFinder.get(fieldProperties.getClazzType());

        Collection<String> addedElements = finder.findAdded(changedObject, originalObject);
        Map<String, Object> changedElements = finder.findChanged(changedObject, originalObject, fieldProperties);
        Collection<String> removedElements = finder.findRemoved(changedObject, originalObject);

        Map<String, Object> resultMap = getComparingMap(removedElements, addedElements, changedElements);

        return Map.of(fieldName, resultMap);
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
            return Map.of(action, elements);
        }
        return Collections.emptyMap();
    }
}
