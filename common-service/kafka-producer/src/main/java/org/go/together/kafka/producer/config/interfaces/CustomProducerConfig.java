package org.go.together.kafka.producer.config.interfaces;

import org.go.together.kafka.producer.beanpostprocessor.ProducerRights;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public interface CustomProducerConfig {
    void configureProducer(String kafkaServer,
                           String kafkaGroupId,
                           ConfigurableListableBeanFactory beanFactory,
                           ProducerRights producerRights);

    String getConsumerId();
}
