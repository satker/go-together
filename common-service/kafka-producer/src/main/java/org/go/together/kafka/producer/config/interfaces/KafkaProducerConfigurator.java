package org.go.together.kafka.producer.config.interfaces;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public interface KafkaProducerConfigurator {
    void configure(String kafkaServer,
                   String kafkaGroupId,
                   ConfigurableListableBeanFactory beanFactory,
                   String consumerId);
}
