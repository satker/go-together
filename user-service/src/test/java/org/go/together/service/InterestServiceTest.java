package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.InterestDto;
import org.go.together.model.Interest;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = RepositoryContext.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9802", "port=9802"})
class InterestServiceTest extends CrudServiceCommonTest<Interest, InterestDto> {
    @Override
    @BeforeEach
    public void init() {
        super.init();
    }

    @Override
    protected InterestDto createDto() {
        return factory.manufacturePojo(InterestDto.class);
    }
}