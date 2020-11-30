package org.go.together.kafka.config;

import org.go.together.dto.Dto;
import org.go.together.kafka.config.consumers.ReadConsumerKafkaConfig;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public abstract class ConsumerKafkaConfig<D extends Dto> extends ReadConsumerKafkaConfig<D> {
    @Override
    public void configureConsumer(String kafkaServer,
                                  String kafkaGroupId,
                                  ConfigurableListableBeanFactory beanFactory) {
        deleteConsumerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        changeConsumerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        readConsumerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        validateConsumerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
    }
}
