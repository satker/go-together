package org.go.together.context;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.RouteInfoClient;
import org.go.together.client.UserClient;
import org.go.together.configuration.H2HibernateConfig;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.kafka.interfaces.producers.crud.ReadKafkaProducer;
import org.go.together.producers.GroupPhotoReadProducer;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;

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
    public ReplyingKafkaTemplate replyingKafkaTemplate() {
        return Mockito.mock(ReplyingKafkaTemplate.class);
    }

    @Bean
    public ReadKafkaProducer<GroupPhotoDto> groupPhotoReadProducer() {
        return Mockito.mock(GroupPhotoReadProducer.class);
    }

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
    public ContentClient contentClient() {
        return Mockito.mock(ContentClient.class);
    }
}
