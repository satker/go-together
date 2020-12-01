package org.go.together.kafka.consumer.impl;

import org.go.together.dto.Dto;
import org.go.together.kafka.consumers.KafkaConsumer;

public abstract class CommonCrudKafkaConsumer<D extends Dto> implements KafkaConsumer<D> {
}
