package org.go.together.find.finders.request;

import org.go.together.dto.FormDto;
import org.go.together.exceptions.RemoteClientFindException;
import org.go.together.find.dto.ClientLocalFieldObject;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class SenderService implements Sender {
    public Collection<Object> send(UUID requestId, ClientLocalFieldObject client, FormDto formDto) {
        return getRemoteResult(requestId, client, formDto);
    }

    private Collection<Object> getRemoteResult(UUID requestId, ClientLocalFieldObject client, FormDto formDto) {
        try {
            return client.getClient().find(requestId, formDto).getResult();
        } catch (Exception e) {
            throw new RemoteClientFindException(e);
        }
    }
}
