package org.go.together.find.finders.request;

import org.go.together.dto.FormDto;
import org.go.together.find.dto.ClientLocalFieldObject;

import java.util.Collection;
import java.util.UUID;

public interface Sender {
    Collection<Object> send(UUID requestId, ClientLocalFieldObject client, FormDto formDto);
}
