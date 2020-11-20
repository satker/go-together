package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.LanguageDto;
import org.go.together.kafka.NotificationEvent;
import org.go.together.model.Language;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ContextConfiguration(classes = RepositoryContext.class)
class LanguageServiceTest extends CrudServiceCommonTest<Language, LanguageDto> {
    @Autowired
    private KafkaTemplate<UUID, NotificationEvent> kafkaTemplate;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        doNothing().when(kafkaTemplate.send(any(), any(), any()));
    }

    @Override
    protected LanguageDto createDto() {
        return factory.manufacturePojo(LanguageDto.class);
    }
}