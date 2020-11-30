package org.go.together.kafka.config.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ValidateConsumerKafkaConfig<D extends Dto> extends DeleteConsumerKafkaConfig {
    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        return props;
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
        return factory;
    }

    private Map<String, Object> getValidateProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "15728640");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        return props;
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
        beanFactory.registerSingleton(getConsumerId() + "ValidateListenerContainerFactory",
                validateListenerContainerFactory(consumerFactory, kafkaTemplate));
    }
}
