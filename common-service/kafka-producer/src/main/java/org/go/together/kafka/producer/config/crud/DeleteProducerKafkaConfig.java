package org.go.together.kafka.producer.config.crud;

import brave.Tracer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.go.together.dto.Dto;
import org.go.together.kafka.producer.beanpostprocessor.ProducerRights;
import org.go.together.kafka.producer.config.interfaces.KafkaProducerConfigurator;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.go.together.kafka.producer.impl.CommonDeleteKafkaProducer;
import org.go.together.kafka.producers.crud.DeleteKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class DeleteProducerKafkaConfig implements KafkaProducerConfigurator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private Map<String, Object> getDeleteProducerConfigs(String kafkaServer) {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    private ProducerFactory<Long, UUID> deleteProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getDeleteProducerConfigs(kafkaServer));
    }

    private KafkaTemplate<Long, UUID> deleteKafkaTemplate(ProducerFactory<Long, UUID> deleteProducerFactory) {
        return new KafkaTemplate<>(deleteProducerFactory);
    }

    public <D extends Dto> void configure(String kafkaServer,
                                          String kafkaGroupId,
                                          ConfigurableListableBeanFactory beanFactory,
                                          ProducerRights<D> producerConfig,
                                          Tracer tracer) {
        if (!producerConfig.isDelete()) {
            return;
        }
        ProducerFactory<Long, UUID> producerFactory = deleteProducerFactory(kafkaServer);
        String producerId = producerConfig.getProducerId();
        beanFactory.registerSingleton(producerId + "DeleteProducerFactory", producerFactory);
        KafkaTemplate<Long, UUID> kafkaTemplate = deleteKafkaTemplate(producerFactory);
        beanFactory.registerSingleton(producerId + "DeleteKafkaTemplate", kafkaTemplate);
        DeleteKafkaProducer<D> commonDeleteKafkaProducer = CommonDeleteKafkaProducer.create(kafkaTemplate, producerId, tracer);
        beanFactory.registerSingleton(producerId + ProducerPostfix.DELETE.getDescription(), commonDeleteKafkaProducer);
        producerConfig.getProducer().setDeleteKafkaProducer(commonDeleteKafkaProducer);
        log.info("Delete producer for " + producerId + " successfully configured!");
    }
}
