package org.go.together.kafka.producer.config.interfaces;

import org.go.together.dto.Dto;
import org.go.together.kafka.producer.beanpostprocessor.ProducerRights;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public interface KafkaProducerConfigurator {
    <D extends Dto> void configure(String kafkaServer,
                                   String kafkaGroupId,
                                   ConfigurableListableBeanFactory beanFactory,
                                   ProducerRights<D> producerConfig);
}
