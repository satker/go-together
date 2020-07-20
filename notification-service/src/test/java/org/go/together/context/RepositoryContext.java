package org.go.together.context;

import org.go.together.configuration.H2HibernateConfig;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
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
        "org.go.together.validation"})
public class RepositoryContext {
}
