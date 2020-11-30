package org.go.together.kafka.config.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.go.together.dto.form.FormDto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class DeleteConsumerKafkaConfig implements CustomConsumerConfig {
    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
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
        return factory;
    }

    protected void deleteConsumerBeanFactoryPostProcessor(String kafkaServer,
                                                          String kafkaGroupId,
                                                          ConfigurableListableBeanFactory beanFactory) {
        ConsumerFactory<UUID, FormDto> consumerFactory = deleteConsumerFactory(kafkaServer, kafkaGroupId);
        //beanFactory.registerSingleton(getConsumerId() + "deleteConsumerFactory", consumerFactory);
        beanFactory.registerSingleton(getConsumerId() + "DeleteListenerContainerFactory",
                deleteListenerContainerFactory(consumerFactory));
    }
}
