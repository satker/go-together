package org.go.together.kafka.producer.config.crud;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.kafka.producer.config.interfaces.CustomProducerConfig;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.go.together.kafka.producer.impl.CommonDeleteKafkaProducer;
import org.go.together.kafka.producers.crud.DeleteKafkaProducer;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DeleteProducerKafkaConfig<D extends Dto> implements CustomProducerConfig {
    private Map<String, Object> getDeleteProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    private ProducerFactory<UUID, UUID> deleteProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getDeleteProducerConfigs(kafkaServer));
    }

    private KafkaTemplate<UUID, UUID> deleteKafkaTemplate(ProducerFactory<UUID, UUID> deleteProducerFactory) {
        return new KafkaTemplate<>(deleteProducerFactory);
    }

    protected void deleteBeanFactoryPostProcessor(String kafkaServer, ConfigurableListableBeanFactory beanFactory) {
        ProducerFactory<UUID, UUID> producerFactory = deleteProducerFactory(kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "DeleteProducerFactory", producerFactory);
        KafkaTemplate<UUID, UUID> kafkaTemplate = deleteKafkaTemplate(producerFactory);
        beanFactory.registerSingleton(getConsumerId() + "DeleteKafkaTemplate", kafkaTemplate);
        DeleteKafkaProducer<D> commonDeleteKafkaProducer = CommonDeleteKafkaProducer.create(kafkaTemplate, getConsumerId());
        beanFactory.registerSingleton(getConsumerId() + ProducerPostfix.DELETE.getDescription(), commonDeleteKafkaProducer);
    }
}
