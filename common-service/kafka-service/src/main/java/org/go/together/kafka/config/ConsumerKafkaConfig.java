package org.go.together.kafka.config;

import org.go.together.dto.Dto;
import org.go.together.kafka.config.consumers.ReadConsumerKafkaConfig;

public abstract class ConsumerKafkaConfig<D extends Dto> extends ReadConsumerKafkaConfig<D> {
}
