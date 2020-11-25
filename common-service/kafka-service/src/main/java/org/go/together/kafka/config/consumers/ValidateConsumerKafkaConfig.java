package org.go.together.kafka.config.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

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

    @Bean
    public BeanFactoryPostProcessor validateConsumerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                             @Value("${kafka.groupId}") String kafkaGroupId,
                                                                             KafkaTemplate<UUID, ValidationMessageDto> validateKafkaTemplate) {
        return beanFactory -> {
            ConsumerFactory<UUID, D> consumerFactory = validateConsumerFactory(kafkaServer, kafkaGroupId);
            beanFactory.registerSingleton(getConsumerId() + "ValidateListenerContainerFactory",
                    validateListenerContainerFactory(consumerFactory, validateKafkaTemplate));
        };
    }

    public abstract String getConsumerId();
}
