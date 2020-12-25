package org.go.together.repository.context;

import org.go.together.repository.configuration.H2HibernateConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Configuration
@Import(H2HibernateConfig.class)
@ComponentScan({
        "org.go.together.repository"
})
public class RepositoryContext {
}
