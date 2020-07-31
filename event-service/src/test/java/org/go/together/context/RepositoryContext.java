package org.go.together.context;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.client.NotificationClient;
import org.go.together.client.UserClient;
import org.go.together.configuration.H2HibernateConfig;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Configuration
@Import(H2HibernateConfig.class)
@ComponentScan(value = {"org.go.together.service",
        "org.go.together.mapper",
        "org.go.together.model",
        "org.go.together.repository",
        "org.go.together.validation",
        "org.go.together.impl"
})
public class RepositoryContext {
    @Bean
    public NotificationClient notificationClient() {
        return Mockito.mock(NotificationClient.class);
    }

    @Bean
    public UserClient userClient() {
        return Mockito.mock(UserClient.class);
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
