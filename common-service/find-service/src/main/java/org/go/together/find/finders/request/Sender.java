package org.go.together.find.finders.request;

import org.go.together.dto.FormDto;
import org.go.together.kafka.producers.FindProducer;

import java.util.Collection;
import java.util.UUID;

public interface Sender {
    Collection<Object> send(UUID requestId, FindProducer<?> client, FormDto formDto);
}
