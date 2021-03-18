package org.go.together.async.enricher;

import brave.Tracer;
import org.go.together.exceptions.ApplicationException;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.go.together.async.config.AsyncTraceableExecutorsConfig.ASYNC_TIMEOUT;

@Component
public class AsyncEnricherImpl implements AsyncEnricher {
    private final TraceableExecutorService traceableExecutorService;
    private final Tracer tracer;
    private final Map<String, List<Supplier<Object>>> map;
    private final Map<String, List<CompletableFuture<Object>>> resultMap;

    public AsyncEnricherImpl(TraceableExecutorService traceableExecutorService,
                             Tracer tracer) {
        this.traceableExecutorService = traceableExecutorService;
        this.tracer = tracer;
        this.map = new ConcurrentHashMap<>();
        this.resultMap = new ConcurrentHashMap<>();
    }

    public void add(Supplier<Object> enricher) {
        String id = getId();
        List<Supplier<Object>> runnables = map.get(id);
        if (runnables == null) {
            runnables = new ArrayList<>();
            runnables.add(enricher);
            map.put(id, runnables);
        } else {
            runnables.add(enricher);
        }
    }

    @Override
    public void add(Runnable enricher) {
        add(() -> {
            enricher.run();
            return null;
        });
    }

    public void start() {
        String id = getId();
        List<Supplier<Object>> suppliers = map.get(id);
        if (suppliers == null) {
            return;
        }
        List<CompletableFuture<Object>> collect = suppliers.stream()
                .map(this::wrapFuture)
                .collect(Collectors.toList());
        resultMap.put(id, collect);
    }

    private CompletableFuture<Object> wrapFuture(Supplier<Object> supplier) {
        return CompletableFuture.supplyAsync(supplier, traceableExecutorService);
    }

    public void clean() {
        String id = getId();
        map.remove(id);
        resultMap.remove(id);
    }

    public void await() {
        String id = getId();
        List<CompletableFuture<Object>> completableFutures = resultMap.get(id);
        if (completableFutures == null) {
            return;
        }
        try {
            CompletableFuture.allOf(completableFutures.toArray(new CompletableFuture[0]))
                    .get(ASYNC_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ApplicationException("Async exception. Cannot execute async enrich: " + e.getMessage());
        }
    }

    @Override
    public void startAndAwait() {
        start();
        await();
        clean();
    }

    private String getId() {
        return tracer.currentSpan().context().traceIdString();
    }
}
