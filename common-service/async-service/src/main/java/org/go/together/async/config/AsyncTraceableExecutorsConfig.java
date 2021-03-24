package org.go.together.async.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.cloud.sleuth.instrument.async.TraceableExecutorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.concurrent.Executors;

@Configuration
public class AsyncTraceableExecutorsConfig {
    public static final int ASYNC_TIMEOUT = 1500;
    private static final int CORES = Runtime.getRuntime().availableProcessors();

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public TraceableExecutorService traceableExecutorService(BeanFactory beanFactory) {
        return new TraceableExecutorService(beanFactory, Executors.newFixedThreadPool(CORES));
    }
}
