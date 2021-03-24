package org.go.together.context;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import org.go.together.configuration.H2HibernateConfig;
import org.go.together.kafka.producer.base.CrudClient;
import org.go.together.kafka.producers.crud.*;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Random;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@Configuration
@Import(H2HibernateConfig.class)
@ComponentScan(value = {
        "org.go.together.service",
        "org.go.together.mapper",
        "org.go.together.model",
        "org.go.together.find",
        "org.go.together.repository",
        "org.go.together.validation",
        "org.go.together.notification",
        "org.go.together.async"
})
public class RepositoryContext {
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public ValidateKafkaProducer validateKafkaProducer() {
        return Mockito.mock(ValidateKafkaProducer.class);
    }

    @Bean
    @Primary
    public CrudClient kafkaCrudClient() {
        return Mockito.mock(CrudClient.class);
    }

    @Bean
    public CreateKafkaProducer createKafkaProducer() {
        return Mockito.mock(CreateKafkaProducer.class);
    }

    @Bean
    public UpdateKafkaProducer updateKafkaProducer() {
        return Mockito.mock(UpdateKafkaProducer.class);
    }

    @Bean
    public ReadKafkaProducer readKafkaProducer() {
        return Mockito.mock(ReadKafkaProducer.class);
    }

    @Bean
    public DeleteKafkaProducer deleteKafkaProducer() {
        return Mockito.mock(DeleteKafkaProducer.class);
    }

    @Bean
    public Tracer tracer() {
        Tracer mock = Mockito.mock(Tracer.class);
        Span span = Mockito.mock(Span.class);
        Span childSpan = Mockito.mock(Span.class);
        Random random = new Random();
        TraceContext traceContext = TraceContext.newBuilder().traceId(random.nextLong())
                .spanId(random.nextLong()).build();
        when(mock.currentSpan()).thenReturn(span);
        when(span.context()).thenReturn(traceContext);
        when(mock.newChild(traceContext)).thenReturn(childSpan);
        when(childSpan.name(anyString())).thenReturn(childSpan);
        when(childSpan.start()).thenReturn(childSpan);
        return mock;
    }
}
