package org.go.together.context;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import org.go.together.configuration.H2HibernateConfig;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.Random;

import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@Configuration
@Import(H2HibernateConfig.class)
@ComponentScan(value = {
        "org.go.together.service",
        "org.go.together.mapper",
        "org.go.together.validation",
        "org.go.together.repository",
        "org.go.together.model",
        "org.go.together.find",
        "org.go.together.async"
})
public class RepositoryContext {
    @Bean
    public Tracer tracer() {
        Tracer mock = Mockito.mock(Tracer.class);
        Span span = Mockito.mock(Span.class);
        Random random = new Random();
        TraceContext traceContext = TraceContext.newBuilder().traceId(random.nextLong())
                .spanId(random.nextLong()).build();
        when(mock.currentSpan()).thenReturn(span);
        when(span.context()).thenReturn(traceContext);
        return mock;
    }
}
