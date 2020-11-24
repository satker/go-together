package org.go.together.kafka.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.IdDto;
import org.go.together.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class CommonKafkaConfig {
    public Map<String, Object> updateConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);

        return props;
    }

    @Bean
    public ConsumerFactory<UUID, IdDto> updateReplyConsumerFactory(@Value("${kafka.server}") String kafkaServer,
                                                                   @Value("${kafka.groupId}") String kafkaGroupId) {
        JsonDeserializer<IdDto> groupPhotoDtoJsonDeserializer = new JsonDeserializer<>();
        groupPhotoDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(updateConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                groupPhotoDtoJsonDeserializer);
    }

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

    @Bean
    public ConsumerFactory<UUID, String> validateReplyConsumerFactory(@Value("${kafka.server}") String kafkaServer,
                                                                      @Value("${kafka.groupId}") String kafkaGroupId) {
        return new DefaultKafkaConsumerFactory<>(createConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                new StringDeserializer());
    }

    private Map<String, Object> getValidateProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "15728640");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        return props;
    }

    @Bean
    public ProducerFactory<UUID, String> validateReplyProducerFactory(@Value("${kafka.server}") String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getValidateProducerConfigs(kafkaServer));
    }

    @Bean
    public KafkaTemplate<UUID, String> validateKafkaTemplate(@Qualifier("validateReplyProducerFactory") ProducerFactory<UUID, String> validateReplyProducerFactory) {
        return new KafkaTemplate<>(validateReplyProducerFactory);
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

    private Map<String, Object> getReadConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        return props;
    }

    @Bean
    public ConsumerFactory<UUID, UUID> readConsumerFactory(@Value("${kafka.server}") String kafkaServer,
                                                           @Value("${kafka.groupId}") String kafkaGroupId) {
        return new DefaultKafkaConsumerFactory<>(getReadConsumerConfigs(kafkaServer, kafkaGroupId));
    }

    private Map<String, Object> getChangeProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<UUID, IdDto> changeReplyProducerFactory(@Value("${kafka.server}") String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getChangeProducerConfigs(kafkaServer));
    }

    @Bean
    public KafkaTemplate<UUID, IdDto> changeKafkaTemplate(@Qualifier("changeReplyProducerFactory") ProducerFactory<UUID, IdDto> changeReplyProducerFactory) {
        return new KafkaTemplate<>(changeReplyProducerFactory);
    }

    private Map<String, Object> getDeleteProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<UUID, UUID> deleteProducerFactory(@Value("${kafka.server}") String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getDeleteProducerConfigs(kafkaServer));
    }

    @Bean
    public KafkaTemplate<UUID, UUID> deleteKafkaTemplate(@Qualifier("deleteProducerFactory") ProducerFactory<UUID, UUID> deleteProducerFactory) {
        return new KafkaTemplate<>(deleteProducerFactory);
    }
}
