package org.go.together.context;

import org.go.together.client.NotificationClient;
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
@ComponentScan({"org.go.together.test", "org.go.together.find", "org.go.together.notification"})
public class RepositoryContext {
    @Bean
    public NotificationClient notificationClient() {
        return Mockito.mock(NotificationClient.class);
    }
}
