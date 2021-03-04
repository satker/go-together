package org.go.together.find.finders.request;

import org.go.together.dto.FormDto;
import org.go.together.exceptions.RemoteClientFindException;
import org.go.together.kafka.producers.FindProducer;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class SenderService implements Sender {
    public Collection<Object> send(FindProducer<?> client, FormDto formDto) {
        return getRemoteResult(client, formDto);
    }

    private Collection<Object> getRemoteResult(FindProducer<?> client, FormDto formDto) {
        try {
            return client.find(formDto).getResult();
        } catch (Exception e) {
            throw new RemoteClientFindException(e);
        }
    }
}
