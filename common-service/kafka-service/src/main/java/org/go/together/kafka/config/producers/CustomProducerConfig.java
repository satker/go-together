package org.go.together.kafka.config.producers;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public interface CustomProducerConfig {
    void configureProducer(String kafkaServer,
                           String kafkaGroupId,
                           ConfigurableListableBeanFactory beanFactory);

    String getConsumerId();
}
