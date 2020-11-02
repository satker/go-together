package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.InterestDto;
import org.go.together.model.Interest;
import org.go.together.notification.streams.NotificationSource;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.test.context.ContextConfiguration;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
class InterestServiceTest extends CrudServiceCommonTest<Interest, InterestDto> {
    @Autowired
    private NotificationSource source;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        MessageChannel messageChannel = Mockito.mock(MessageChannel.class);
        when(source.output()).thenReturn(messageChannel);
        when(messageChannel.send(any())).thenReturn(true);
    }

    @Override
    protected InterestDto createDto() {
        return factory.manufacturePojo(InterestDto.class);
    }
}