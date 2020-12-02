package org.go.together.kafka.consumer.config.interfaces;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public interface CustomConsumerConfig {
    void configureConsumer(String kafkaServer,
                           String kafkaGroupId,
                           ConfigurableListableBeanFactory beanFactory);

    String getConsumerId();
}
