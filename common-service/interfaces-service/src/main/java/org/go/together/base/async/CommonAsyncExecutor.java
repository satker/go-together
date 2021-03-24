package org.go.together.base.async;

public interface CommonAsyncExecutor {
    void add(String name, Runnable enricher);

    void start();

    void clean();

    void await();

    void startAndAwait();
}
