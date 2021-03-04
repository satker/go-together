package org.go.together.find.finders.request;

import org.go.together.dto.FormDto;
import org.go.together.kafka.producers.FindProducer;

import java.util.Collection;

public interface Sender {
    Collection<Object> send(FindProducer<?> client, FormDto formDto);
}
