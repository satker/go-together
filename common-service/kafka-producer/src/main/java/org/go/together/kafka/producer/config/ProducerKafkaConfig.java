package org.go.together.kafka.producer.config;

import org.go.together.dto.Dto;
import org.go.together.kafka.producer.beanpostprocessor.ProducerRights;
import org.go.together.kafka.producer.config.crud.CreateProducerKafkaConfig;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public abstract class ProducerKafkaConfig<D extends Dto> extends CreateProducerKafkaConfig<D> {
    public static <D extends Dto> ProducerKafkaConfig<D> create(String consumerId) {
        return new ProducerKafkaConfig<>() {
            @Override
            public String getConsumerId() {
                return consumerId;
            }
        };
    }

    public void configureProducer(String kafkaServer,
                                  String kafkaGroupId,
                                  ConfigurableListableBeanFactory beanFactory,
                                  ProducerRights producerRights) {
        if (producerRights.isCreate()) {
            createProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        }
        if (producerRights.isDelete()) {
            deleteBeanFactoryPostProcessor(kafkaServer, beanFactory);
        }
        if (producerRights.isFind()) {
            findProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        }
        if (producerRights.isRead()) {
            readProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        }
        if (producerRights.isUpdate()) {
            updateProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        }
        if (producerRights.isValidate()) {
            validateProducerBeanFactoryPostProcessor(kafkaServer, kafkaGroupId, beanFactory);
        }
    }
}
