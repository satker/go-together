package org.go.together.kafka.config.producers;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DeleteProducerKafkaConfig {
    private Map<String, Object> getProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    private ProducerFactory<UUID, UUID> deleteProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getProducerConfigs(kafkaServer));
    }

    private KafkaTemplate<UUID, UUID> deleteKafkaTemplate(ProducerFactory<UUID, UUID> deleteProducerFactory) {
        return new KafkaTemplate<>(deleteProducerFactory);
    }

    @Bean
    public BeanFactoryPostProcessor deleteProducerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer) {
        return beanFactory -> {
            ProducerFactory<UUID, UUID> producerFactory = deleteProducerFactory(kafkaServer);
            KafkaTemplate<UUID, UUID> kafkaTemplate = deleteKafkaTemplate(producerFactory);
            beanFactory.registerSingleton(getConsumerId() + "DeleteKafkaTemplate", kafkaTemplate);
        };
    }

    public abstract String getConsumerId();
}
