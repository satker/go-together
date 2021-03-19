package org.go.together.async.common;

import brave.Span;
import brave.Tracer;
import org.go.together.async.config.AsyncTraceableExecutorsConfig;
import org.go.together.base.async.CommonAsyncExecutor;
import org.go.together.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class CommonAsyncExecutorImpl implements CommonAsyncExecutor {
    private final Map<String, List<Supplier<Object>>> map;
    private final Map<String, List<CompletableFuture<Object>>> resultMap;
    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected CommonAsyncExecutorImpl() {
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
    public void add(String name, Runnable enricher) {
        add(() -> {
            Span span = getTracer().newChild(getTracer().currentSpan().context()).name(name).start();
            try {
                enricher.run();
            } finally {
                span.finish();
            }
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
        return CompletableFuture.supplyAsync(supplier, getTraceableExecutorService());
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
                    .get(AsyncTraceableExecutorsConfig.ASYNC_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new ApplicationException("Async exception. Cannot execute async enrich: " + e.getMessage());
        }
    }

    @Override
    public void startAndAwait() {
        if (map.get(getId()) == null) {
            return;
        }
        start();
        await();
        clean();
    }

    private String getId() {
        return getTracer().currentSpan().context().traceIdString();
    }

    protected abstract Tracer getTracer();

    protected abstract TraceableExecutorService getTraceableExecutorService();
}
