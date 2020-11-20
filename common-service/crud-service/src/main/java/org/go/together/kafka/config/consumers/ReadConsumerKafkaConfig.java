package org.go.together.kafka.config.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ReadConsumerKafkaConfig<D extends Dto> {
    protected Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        return props;
    }

    protected Map<String, Object> getProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<UUID, UUID> readConsumerFactory(@Value("${kafka.server}") String kafkaServer,
                                                           @Value("${kafka.groupId}") String kafkaGroupId) {
        return new DefaultKafkaConsumerFactory<>(getConsumerConfigs(kafkaServer, kafkaGroupId));
    }

    public ConcurrentKafkaListenerContainerFactory<UUID, UUID> readListenerContainerFactory(ConsumerFactory<UUID, UUID> readConsumerFactory,
                                                                                            KafkaTemplate<UUID, D> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<UUID, UUID> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(readConsumerFactory);
        factory.setReplyTemplate(kafkaTemplate);
        return factory;
    }

    @Bean
    public BeanFactoryPostProcessor readConsumerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                         ConsumerFactory<UUID, UUID> readConsumerFactory) {
        return beanFactory -> {
            ProducerFactory<UUID, D> producerFactory = readReplyProducerFactory(kafkaServer);
            beanFactory.registerSingleton(getConsumerId() + "ReadReplyProducerFactory", producerFactory);
            KafkaTemplate<UUID, D> kafkaTemplate = readKafkaTemplate(producerFactory);
            beanFactory.registerSingleton(getConsumerId() + "ReadKafkaTemplate", kafkaTemplate);
            beanFactory.registerSingleton(getConsumerId() + "ReadListenerContainerFactory",
                    readListenerContainerFactory(readConsumerFactory, kafkaTemplate));
        };
    }

    public ProducerFactory<UUID, D> readReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getProducerConfigs(kafkaServer));
    }

    public KafkaTemplate<UUID, D> readKafkaTemplate(ProducerFactory<UUID, D> readReplyProducerFactory) {
        return new KafkaTemplate<>(readReplyProducerFactory);
    }

    public abstract String getConsumerId();
}
