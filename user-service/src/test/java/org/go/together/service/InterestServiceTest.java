package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.InterestDto;
import org.go.together.kafka.NotificationEvent;
import org.go.together.model.Interest;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@ContextConfiguration(classes = RepositoryContext.class)
class InterestServiceTest extends CrudServiceCommonTest<Interest, InterestDto> {
    @Autowired
    private KafkaTemplate<UUID, NotificationEvent> kafkaTemplate;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        doNothing().when(kafkaTemplate.send(any(), any(), any()));
    }

    @Override
    protected InterestDto createDto() {
        return factory.manufacturePojo(InterestDto.class);
    }
}