package org.go.together.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.go.together.dto.form.FormDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class CommonKafkaConfig {

    public Map<String, Object> createConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);

        return props;
    }

    @Bean
    public ConsumerFactory<UUID, IdDto> createReplyConsumerFactory(@Value("${kafka.server}") String kafkaServer,
                                                                   @Value("${kafka.groupId}") String kafkaGroupId) {
        JsonDeserializer<IdDto> groupPhotoDtoJsonDeserializer = new JsonDeserializer<>();
        groupPhotoDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(createConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                groupPhotoDtoJsonDeserializer);
    }

    private Map<String, Object> getFindProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<UUID, ResponseDto<Object>> findReplyProducerFactory(@Value("${kafka.server}") String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getFindProducerConfigs(kafkaServer));
    }

    @Bean
    public KafkaTemplate<UUID, ResponseDto<Object>> findKafkaTemplate(@Qualifier("findReplyProducerFactory") ProducerFactory<UUID, ResponseDto<Object>> findReplyProducerFactory) {
        return new KafkaTemplate<>(findReplyProducerFactory);
    }

    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
    }

    @Bean
    public ConsumerFactory<UUID, FormDto> findConsumerFactory(@Value("${kafka.server}") String kafkaServer,
                                                              @Value("${kafka.groupId}") String kafkaGroupId) {
        JsonDeserializer<FormDto> formDtoJsonDeserializer = new JsonDeserializer<>();
        formDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(getConsumerConfigs(kafkaServer, kafkaGroupId), new UUIDDeserializer(),
                formDtoJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<UUID, FormDto> findListenerContainerFactory(ConsumerFactory<UUID, FormDto> findConsumerFactory,
                                                                                               KafkaTemplate<UUID, ResponseDto<Object>> findKafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<UUID, FormDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(findConsumerFactory);
        factory.setReplyTemplate(findKafkaTemplate);
        return factory;
    }
}
