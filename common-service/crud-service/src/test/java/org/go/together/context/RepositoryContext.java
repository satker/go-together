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

import java.util.Random;

import static org.mockito.Mockito.when;

@EnableAutoConfiguration
@Configuration
@Import(H2HibernateConfig.class)
@ComponentScan({
        "org.go.together.test",
        "org.go.together.find",
        "org.go.together.notification",
        "org.go.together.validation",
        "org.go.together.async"
})
public class RepositoryContext {
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
        Random random = new Random();
        TraceContext traceContext = TraceContext.newBuilder().traceId(random.nextLong())
                .spanId(random.nextLong()).build();
        when(mock.currentSpan()).thenReturn(span);
        when(span.context()).thenReturn(traceContext);
        return mock;
    }
}
