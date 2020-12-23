package org.go.together.kafka.consumer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ReadConsumerKafkaConfig<D extends Dto> extends ChangeConsumerKafkaConfig<D> {
    private Map<String, Object> getProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "15728640");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        return props;
    }

    private Map<String, Object> getReadConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        return props;
    }

    public ConsumerFactory<UUID, UUID> readConsumerFactory(String kafkaServer,
                                                           String kafkaGroupId) {
        return new DefaultKafkaConsumerFactory<>(getReadConsumerConfigs(kafkaServer, kafkaGroupId));
    }

    private ConcurrentKafkaListenerContainerFactory<UUID, UUID> readListenerContainerFactory(ConsumerFactory<UUID, UUID> readConsumerFactory,
                                                                                             KafkaTemplate<UUID, D> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<UUID, UUID> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(readConsumerFactory);
        factory.setReplyTemplate(kafkaTemplate);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(500L, 2L)));
        return factory;
    }

    public void readConsumerBeanFactoryPostProcessor(String kafkaServer, String kafkaGroupId, ConfigurableListableBeanFactory beanFactory) {
        ProducerFactory<UUID, D> producerFactory = readReplyProducerFactory(kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "ReadReplyProducerFactory", producerFactory);
        KafkaTemplate<UUID, D> kafkaTemplate = readKafkaTemplate(producerFactory);
        beanFactory.registerSingleton(getConsumerId() + "ReadKafkaTemplate", kafkaTemplate);
        beanFactory.registerSingleton(getConsumerId() + "ReadListenerContainerFactory",
                readListenerContainerFactory(readConsumerFactory(kafkaServer, kafkaGroupId), kafkaTemplate));
    }

    private ProducerFactory<UUID, D> readReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getProducerConfigs(kafkaServer));
    }

    private KafkaTemplate<UUID, D> readKafkaTemplate(ProducerFactory<UUID, D> readReplyProducerFactory) {
        return new KafkaTemplate<>(readReplyProducerFactory);
    }
}
