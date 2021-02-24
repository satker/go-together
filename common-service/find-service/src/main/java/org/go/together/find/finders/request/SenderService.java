package org.go.together.find.finders.request;

import org.go.together.dto.FormDto;
import org.go.together.exceptions.RemoteClientFindException;
import org.go.together.kafka.producers.FindProducer;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class SenderService implements Sender {
    public Collection<Object> send(UUID requestId, FindProducer<?> client, FormDto formDto) {
        return getRemoteResult(requestId, client, formDto);
    }

    private Collection<Object> getRemoteResult(UUID requestId, FindProducer<?> client, FormDto formDto) {
        try {
            return client.find(requestId, formDto).getResult();
        } catch (Exception e) {
            throw new RemoteClientFindException(e);
        }
    }
}
