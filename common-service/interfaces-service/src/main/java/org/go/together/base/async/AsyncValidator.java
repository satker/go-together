package org.go.together.base.async;

import org.go.together.dto.Dto;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface AsyncValidator {
    <D extends Dto> Set<String> validate(BiFunction<Map.Entry<String, Function<D, ?>>, D, Callable<String>> wrapValidation,
                                         Map<String, Function<D, ?>> mapsForCheck,
                                         D dto);
}
