package org.go.together.context;

import org.go.together.client.LocationClient;
import org.go.together.client.RouteInfoClient;
import org.go.together.client.UserClient;
import org.go.together.configuration.H2HibernateConfig;
import org.go.together.kafka.base.KafkaCrudClient;
import org.go.together.kafka.interfaces.producers.crud.*;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

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
        "org.go.together.notification"
})
public class RepositoryContext {
    @Bean
    public UserClient userClient() {
        return Mockito.mock(UserClient.class);
    }

    @Bean
    public RouteInfoClient routeInfoClient() {
        return Mockito.mock(RouteInfoClient.class);
    }

    @Bean
    public LocationClient locationClient() {
        return Mockito.mock(LocationClient.class);
    }

    @Bean
    @Primary
    public ValidateKafkaProducer validateKafkaProducer() {
        return Mockito.mock(ValidateKafkaProducer.class);
    }

    @Bean
    @Primary
    public KafkaCrudClient kafkaCrudClient() {
        return Mockito.mock(KafkaCrudClient.class);
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
}
