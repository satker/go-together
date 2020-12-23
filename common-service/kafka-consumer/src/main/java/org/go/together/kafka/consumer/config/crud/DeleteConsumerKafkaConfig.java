package org.go.together.kafka.consumer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.go.together.dto.FormDto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;
import java.util.UUID;

import static org.go.together.enums.TopicKafkaPostfix.DELETE;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

public abstract class DeleteConsumerKafkaConfig extends FindConsumerKafkaConfig {
    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private ConsumerFactory<UUID, FormDto> deleteConsumerFactory(String kafkaServer, String kafkaGroupId) {
        JsonDeserializer<FormDto> formDtoJsonDeserializer = new JsonDeserializer<>();
        formDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(getConsumerConfigs(kafkaServer, kafkaGroupId), new UUIDDeserializer(),
                formDtoJsonDeserializer);
    }

    private ConcurrentKafkaListenerContainerFactory<UUID, FormDto> deleteListenerContainerFactory(ConsumerFactory<UUID, FormDto> deleteConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<UUID, FormDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(deleteConsumerFactory);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(500L, 2L)));
        return factory;
    }

    protected void deleteConsumerBeanFactoryPostProcessor(String kafkaServer,
                                                          String kafkaGroupId,
                                                          ConfigurableListableBeanFactory beanFactory) {
        ConsumerFactory<UUID, FormDto> consumerFactory = deleteConsumerFactory(kafkaServer, kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + DELETE + LISTENER_FACTORY,
                deleteListenerContainerFactory(consumerFactory));
    }
}
