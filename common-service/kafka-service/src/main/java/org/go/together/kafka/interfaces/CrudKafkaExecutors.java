package org.go.together.kafka.interfaces;

import org.go.together.dto.Dto;
import org.go.together.kafka.interfaces.producers.ReplyKafkaProducer;

import java.util.Collections;
import java.util.Map;
import java.util.function.Consumer;

public interface CrudKafkaExecutors {
    void execute(Map<ReplyKafkaProducer, Consumer<Dto>> producers);

    default void execute(ReplyKafkaProducer producer, Consumer<Dto> resultAction) {
        execute(Collections.singletonMap(producer, resultAction));
    }
}
