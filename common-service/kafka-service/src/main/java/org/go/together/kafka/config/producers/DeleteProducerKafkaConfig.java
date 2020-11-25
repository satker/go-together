package org.go.together.kafka.config.producers;

import org.go.together.dto.Dto;
import org.go.together.kafka.impl.producers.CommonDeleteKafkaProducer;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;

public abstract class DeleteProducerKafkaConfig<D extends Dto> {
    @Bean
    public BeanFactoryPostProcessor DeleteBeanFactoryPostProcessor(KafkaTemplate<UUID, UUID> kafkaTemplate) {
        return beanFactory -> {
            CommonDeleteKafkaProducer<D> commonDeleteKafkaProducer = new CommonDeleteKafkaProducer<>(kafkaTemplate) {
                @Override
                public String getTopicId() {
                    return getConsumerId();
                }
            };
            beanFactory.registerSingleton(getConsumerId() + "DeleteKafkaProducer", commonDeleteKafkaProducer);
        };
    }

    public abstract String getConsumerId();
}
