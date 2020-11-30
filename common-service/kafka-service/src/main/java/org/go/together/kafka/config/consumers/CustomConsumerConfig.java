package org.go.together.kafka.config.consumers;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public interface CustomConsumerConfig {
    void configureConsumer(String kafkaServer,
                           String kafkaGroupId,
                           ConfigurableListableBeanFactory beanFactory);

    String getConsumerId();
}
