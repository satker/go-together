package org.go.together.async.enricher;

import java.util.function.Supplier;

public interface AsyncEnricher {
    void add(Supplier<Object> enricher);

    void add(Runnable enricher);

    void start();

    void clean();

    void await();

    void startAndAwait();
}
