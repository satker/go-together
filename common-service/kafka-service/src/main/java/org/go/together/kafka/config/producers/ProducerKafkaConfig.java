package org.go.together.kafka.config.producers;

import org.go.together.dto.Dto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public abstract class ProducerKafkaConfig<D extends Dto> extends CreateProducerKafkaConfig<D> {
    public void configureProducer(String kafkaServer,
                                  String kafkaGroupId,
                                  ConfigurableListableBeanFactory beanFactory) {
        createProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        deleteBeanFactoryPostProcessor(kafkaServer, beanFactory);
        findProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        readProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        updateProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        validateProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
    }
}
