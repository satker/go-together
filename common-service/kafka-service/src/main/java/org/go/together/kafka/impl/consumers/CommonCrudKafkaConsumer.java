package org.go.together.kafka.impl.consumers;

import org.go.together.dto.Dto;
import org.go.together.kafka.interfaces.consumers.KafkaConsumer;

public abstract class CommonCrudKafkaConsumer<D extends Dto> implements KafkaConsumer<D> {
}
