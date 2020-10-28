package org.go.together.context;

import org.go.together.configuration.H2HibernateConfig;
import org.go.together.notification.streams.NotificationSource;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Configuration
@Import(H2HibernateConfig.class)
@ComponentScan(value = {
        "org.go.together.service",
        "org.go.together.mapper",
        "org.go.together.model",
        "org.go.together.find",
        "org.go.together.validation",
        "org.go.together.notification",
        "org.go.together.repository"
})
public class RepositoryContext {
    @Bean
    public NotificationSource source() {
        return Mockito.mock(NotificationSource.class);
    }
}
