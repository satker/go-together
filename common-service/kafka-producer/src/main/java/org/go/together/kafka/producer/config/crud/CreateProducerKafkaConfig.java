package org.go.together.kafka.producer.config.crud;

import brave.Tracer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producer.beanpostprocessor.ProducerRights;
import org.go.together.kafka.producer.config.interfaces.KafkaProducerConfigurator;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.go.together.kafka.producer.impl.CommonCreateKafkaProducer;
import org.go.together.kafka.producers.ReplyKafkaProducer;
import org.go.together.kafka.producers.crud.CreateKafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;


@Component
public class CreateProducerKafkaConfig implements KafkaProducerConfigurator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private <D extends Dto> ProducerFactory<UUID, D> createProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> createConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private <D extends Dto> ReplyingKafkaTemplate<UUID, D, IdDto> createReplyingKafkaTemplate(KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer,
                                                                                              String kafkaServer) {
        return new ReplyingKafkaTemplate<>(createProducerFactory(kafkaServer), createRepliesContainer);
    }

    private KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer(ConsumerFactory<UUID, IdDto> createReplyConsumerFactory,
                                                                              String kafkaGroupId,
                                                                              String consumerId) {
        String createReplyTopicId = getCreateReplyTopicId(consumerId, kafkaGroupId);
        ContainerProperties containerProperties = new ContainerProperties(createReplyTopicId);
        return new KafkaMessageListenerContainer<>(createReplyConsumerFactory, containerProperties);
    }

    private ConsumerFactory<UUID, IdDto> createReplyConsumerFactory(String kafkaServer,
                                                                    String kafkaGroupId) {
        JsonDeserializer<IdDto> groupPhotoDtoJsonDeserializer = new JsonDeserializer<>();
        groupPhotoDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(createConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                groupPhotoDtoJsonDeserializer);
    }

    public <D extends Dto> void configure(String kafkaServer,
                                          String kafkaGroupId,
                                          ConfigurableListableBeanFactory beanFactory,
                                          ProducerRights<D> producerConfig,
                                          Tracer tracer) {
        if (!producerConfig.isCreate()) {
            return;
        }
        ConsumerFactory<UUID, IdDto> replyConsumerFactory = createReplyConsumerFactory(kafkaServer, kafkaGroupId);
        String producerId = producerConfig.getProducerId();
        KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer = createRepliesContainer(replyConsumerFactory, kafkaGroupId, producerId);
        ReplyingKafkaTemplate<UUID, D, IdDto> createReplyingKafkaTemplate = createReplyingKafkaTemplate(createRepliesContainer, kafkaServer);
        beanFactory.registerSingleton(producerId + "CreateReplyingKafkaTemplate", createReplyingKafkaTemplate);
        CreateKafkaProducer<D> commonCreateKafkaProducer = CommonCreateKafkaProducer.create(createReplyingKafkaTemplate, kafkaGroupId, producerId, tracer);
        beanFactory.registerSingleton(producerId + ProducerPostfix.CREATE.getDescription(), commonCreateKafkaProducer);
        producerConfig.getProducer().setCreateKafkaProducer(commonCreateKafkaProducer);
        log.info("Create producer for " + producerId + " successfully configured!");
    }

    private String getCreateReplyTopicId(String consumerId, String kafkaGroupId) {
        return consumerId + TopicKafkaPostfix.CREATE + ReplyKafkaProducer.KAFKA_REPLY_ID + kafkaGroupId;
    }
}
