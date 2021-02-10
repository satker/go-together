package org.go.together.notification.comparemapper.impl;

import org.go.together.compare.FieldProperties;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.comparators.transformers.interfaces.Transformer;
import org.go.together.notification.comparemapper.interfaces.CompareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CommonCompareMapper implements CompareMapper {
    private Transformer<Comparator> compareTransformer;

    @Autowired
    public void setCompareTransformer(Transformer<Comparator> compareTransformer) {
        this.compareTransformer = compareTransformer;
    }

    @Override
    public Map<String, Object> transform(String fieldName, Object originalObject, Object changedObject, FieldProperties fieldProperties) {
        return compareTransformer.get(fieldName, originalObject, changedObject, fieldProperties)
                .compare(fieldName, originalObject, changedObject, fieldProperties);
    }
}
