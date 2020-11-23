package org.go.together.kafka.config;

import org.go.together.dto.Dto;
import org.go.together.kafka.config.producers.CreateProducerKafkaConfig;

public abstract class ProducerKafkaConfig<D extends Dto> extends CreateProducerKafkaConfig<D> {
}
