package org.go.together.notification.comparemapper.impl;

import org.go.together.compare.ComparableDto;
import org.go.together.compare.ComparingObject;
import org.go.together.notification.comparators.interfaces.Comparator;
import org.go.together.notification.comparators.transformers.interfaces.Transformer;
import org.go.together.notification.comparemapper.interfaces.CompareMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.go.together.utils.ReflectionUtils.getClazz;

@Component
public class CommonCompareMapper implements CompareMapper {
    private Transformer<Comparator> compareTransformer;

    @Autowired
    public void setCompareTransformer(Transformer<Comparator> compareTransformer) {
        this.compareTransformer = compareTransformer;
    }

    @Override
    public Map<String, Object> transform(String fieldName, Object originalObject, Object changedObject, ComparingObject fieldProperties) {
        if (ComparableDto.class.isAssignableFrom(getClazz(fieldProperties.getClazzType()))) {
            ComparableDto comparableDtoObject = (ComparableDto) originalObject;
            ComparableDto anotherComparableDtoObject = (ComparableDto) changedObject;
            if (fieldProperties.getIdCompare()) {
                return compareTransformer.get(fieldName, originalObject, changedObject, fieldProperties)
                        .compare(fieldName, comparableDtoObject.getId(), anotherComparableDtoObject.getId(), fieldProperties);
            }
        }
        return compareTransformer.get(fieldName, originalObject, changedObject, fieldProperties)
                .compare(fieldName, originalObject, changedObject, fieldProperties);
    }

}
