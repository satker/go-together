package org.go.together.kafka.consumer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.kafka.consumer.config.interfaces.CustomConsumerConfig;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class FindConsumerKafkaConfig implements CustomConsumerConfig {
    private Map<String, Object> getFindProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
    }

    private ProducerFactory<UUID, ResponseDto<Object>> findReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getFindProducerConfigs(kafkaServer));
    }

    private KafkaTemplate<UUID, ResponseDto<Object>> findKafkaTemplate(ProducerFactory<UUID, ResponseDto<Object>> findReplyProducerFactory) {
        return new KafkaTemplate<>(findReplyProducerFactory);
    }

    private ConsumerFactory<UUID, FormDto> findConsumerFactory(String kafkaServer, String kafkaGroupId) {
        JsonDeserializer<FormDto> formDtoJsonDeserializer = new JsonDeserializer<>();
        formDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(getConsumerConfigs(kafkaServer, kafkaGroupId), new UUIDDeserializer(),
                formDtoJsonDeserializer);
    }

    private ConcurrentKafkaListenerContainerFactory<UUID, FormDto> findListenerContainerFactory(ConsumerFactory<UUID, FormDto> findConsumerFactory,
                                                                                                KafkaTemplate<UUID, ResponseDto<Object>> findKafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<UUID, FormDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(findConsumerFactory);
        factory.setReplyTemplate(findKafkaTemplate);
        return factory;
    }

    public void findConsumerBeanFactoryPostProcessor(String kafkaServer, String kafkaGroupId, ConfigurableListableBeanFactory beanFactory) {
        ProducerFactory<UUID, ResponseDto<Object>> replyProducerFactory = findReplyProducerFactory(kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "FindReplyProducerFactory", replyProducerFactory);
        KafkaTemplate<UUID, ResponseDto<Object>> kafkaTemplate = findKafkaTemplate(replyProducerFactory);
        beanFactory.registerSingleton(getConsumerId() + "FindKafkaTemplate", kafkaTemplate);
        ConsumerFactory<UUID, FormDto> consumerFactory = findConsumerFactory(kafkaServer, kafkaGroupId);
        ConcurrentKafkaListenerContainerFactory<UUID, FormDto> listenerContainerFactory =
                findListenerContainerFactory(consumerFactory, kafkaTemplate);
        beanFactory.registerSingleton(getConsumerId() + "FindListenerContainerFactory", listenerContainerFactory);
    }
}
