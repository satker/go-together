package org.go.together.async.validation;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.async.AsyncValidator;
import org.go.together.dto.Dto;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.go.together.async.config.AsyncTraceableExecutorsConfig.ASYNC_TIMEOUT;

@Component
public class AsyncValidatorImpl implements AsyncValidator {
    private static final String ERROR_MESSAGE = "Cannot validate async task";

    private final TraceableExecutorService traceableExecutorService;

    public AsyncValidatorImpl(TraceableExecutorService traceableExecutorService) {
        this.traceableExecutorService = traceableExecutorService;
    }

    @Override
    public <D extends Dto> Set<String> validate(BiFunction<Map.Entry<String, Function<D, ?>>, D, Callable<String>> wrapValidation,
                                                Map<String, Function<D, ?>> mapsForCheck,
                                                D dto) {
        Set<Callable<String>> collect = mapsForCheck.entrySet().stream()
                .map(entry -> wrapValidation.apply(entry, dto))
                .collect(Collectors.toSet());
        try {
            return traceableExecutorService.invokeAll(collect).stream()
                    .map(this::getAsync)
                    .filter(StringUtils::isNotBlank)
                    .collect(Collectors.toSet());

        } catch (InterruptedException e) {
            return Set.of(ERROR_MESSAGE);
        }
    }

    private String getAsync(Future<String> future) {
        try {
            return future.get(ASYNC_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return ERROR_MESSAGE;
        }
    }
}
