package org.go.together.validation.validators;

import org.go.together.interfaces.ImplFinder;
import org.go.together.utils.ReflectionUtils;
import org.go.together.validation.validators.interfaces.ObjectValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ValidatorImplFinder implements ImplFinder<ObjectValidator> {
    private Map<Class<?>, ObjectValidator> classComparatorMap;

    @Override
    @Autowired
    public void setImpl(List<ObjectValidator> objectValidators) {
        this.classComparatorMap = objectValidators.stream()
                .collect(Collectors.toMap(comparator -> ReflectionUtils.getParametrizedInterface(comparator.getClass(), 0),
                        Function.identity()));
    }

    @Override
    public Map<Class<?>, ObjectValidator> getImpls() {
        return classComparatorMap;
    }
}
