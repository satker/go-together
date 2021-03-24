package org.go.together.kafka.consumer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.go.together.dto.Dto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;
import java.util.UUID;

import static org.go.together.interfaces.TopicKafkaPostfix.READ;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

public abstract class ReadConsumerKafkaConfig<D extends Dto> extends ChangeConsumerKafkaConfig<D> {
    private Map<String, Object> getProducerConfigs(String kafkaServer) {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "15728640",
                ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"
        );
    }

    private Map<String, Object> getReadConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class
        );
    }

    public ConsumerFactory<Long, UUID> readConsumerFactory(String kafkaServer,
                                                           String kafkaGroupId) {
        return new DefaultKafkaConsumerFactory<>(getReadConsumerConfigs(kafkaServer, kafkaGroupId));
    }

    private ConcurrentKafkaListenerContainerFactory<Long, UUID> readListenerContainerFactory(ConsumerFactory<Long, UUID> readConsumerFactory,
                                                                                             KafkaTemplate<Long, D> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<Long, UUID> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(readConsumerFactory);
        factory.setReplyTemplate(kafkaTemplate);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(500L, 2L)));
        return factory;
    }

    public void readConsumerBeanFactoryPostProcessor(String kafkaServer, String kafkaGroupId, ConfigurableListableBeanFactory beanFactory) {
        ProducerFactory<Long, D> producerFactory = readReplyProducerFactory(kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "ReadReplyProducerFactory", producerFactory);
        KafkaTemplate<Long, D> kafkaTemplate = readKafkaTemplate(producerFactory);
        beanFactory.registerSingleton(getConsumerId() + "ReadKafkaTemplate", kafkaTemplate);
        beanFactory.registerSingleton(getConsumerId() + READ + LISTENER_FACTORY,
                readListenerContainerFactory(readConsumerFactory(kafkaServer, kafkaGroupId), kafkaTemplate));
    }

    private ProducerFactory<Long, D> readReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getProducerConfigs(kafkaServer));
    }

    private KafkaTemplate<Long, D> readKafkaTemplate(ProducerFactory<Long, D> readReplyProducerFactory) {
        return new KafkaTemplate<>(readReplyProducerFactory);
    }
}
