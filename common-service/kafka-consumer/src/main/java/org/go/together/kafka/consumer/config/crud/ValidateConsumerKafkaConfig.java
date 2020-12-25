package org.go.together.kafka.consumer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;
import java.util.UUID;

import static org.go.together.enums.TopicKafkaPostfix.VALIDATE;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

public abstract class ValidateConsumerKafkaConfig<D extends Dto> extends DeleteConsumerKafkaConfig {
    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class
        );
    }

    private Map<String, Object> getValidateProducerConfigs(String kafkaServer) {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "15728640",
                ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy"
        );
    }

    private ConsumerFactory<UUID, D> validateConsumerFactory(String kafkaServer,
                                                             String kafkaGroupId) {
        JsonDeserializer<D> validateDtoJsonDeserializer = new JsonDeserializer<>();
        validateDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(
                getConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                validateDtoJsonDeserializer);
    }

    private ConcurrentKafkaListenerContainerFactory<UUID, D> validateListenerContainerFactory(ConsumerFactory<UUID, D> validateConsumerFactory,
                                                                                              KafkaTemplate<UUID, ValidationMessageDto> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<UUID, D> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(validateConsumerFactory);
        factory.setReplyTemplate(kafkaTemplate);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(500L, 2L)));
        return factory;
    }

    public ProducerFactory<UUID, ValidationMessageDto> validateReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getValidateProducerConfigs(kafkaServer));
    }

    public KafkaTemplate<UUID, ValidationMessageDto> validateKafkaTemplate(ProducerFactory<UUID, ValidationMessageDto> validateReplyProducerFactory) {
        return new KafkaTemplate<>(validateReplyProducerFactory);
    }

    protected void validateConsumerBeanFactoryPostProcessor(String kafkaServer, String kafkaGroupId, ConfigurableListableBeanFactory beanFactory) {
        ConsumerFactory<UUID, D> consumerFactory = validateConsumerFactory(kafkaServer, kafkaGroupId);
        ProducerFactory<UUID, ValidationMessageDto> producerFactory = validateReplyProducerFactory(kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "ValidateReplyProducerFactory", producerFactory);
        KafkaTemplate<UUID, ValidationMessageDto> kafkaTemplate = validateKafkaTemplate(producerFactory);
        beanFactory.registerSingleton(getConsumerId() + "ValidateKafkaTemplate", kafkaTemplate);
        beanFactory.registerSingleton(getConsumerId() + VALIDATE + LISTENER_FACTORY,
                validateListenerContainerFactory(consumerFactory, kafkaTemplate));
    }
}
