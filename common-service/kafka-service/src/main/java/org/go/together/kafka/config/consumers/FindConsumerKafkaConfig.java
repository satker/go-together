package org.go.together.kafka.config.consumers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.springframework.beans.factory.annotation.Qualifier;
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

public abstract class FindConsumerKafkaConfig extends DeleteConsumerKafkaConfig {
    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
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

    @Bean
    public BeanFactoryPostProcessor findConsumerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                         @Value("${kafka.groupId}") String kafkaGroupId,
                                                                         @Qualifier("findKafkaTemplate") KafkaTemplate<UUID, ResponseDto<Object>> findKafkaTemplate) {
        return beanFactory -> {
            ConsumerFactory<UUID, FormDto> consumerFactory = findConsumerFactory(kafkaServer, kafkaGroupId);
            //beanFactory.registerSingleton(getConsumerId() + "findConsumerFactory", consumerFactory);
            beanFactory.registerSingleton(getConsumerId() + "FindListenerContainerFactory",
                    findListenerContainerFactory(consumerFactory, findKafkaTemplate));
        };
    }
}
