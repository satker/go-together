package org.go.together.kafka.producer.config.crud;

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
import org.go.together.kafka.producer.impl.CommonUpdateKafkaProducer;
import org.go.together.kafka.producers.ReplyKafkaProducer;
import org.go.together.kafka.producers.crud.UpdateKafkaProducer;
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
public class UpdateProducerKafkaConfig implements KafkaProducerConfigurator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private <D extends Dto> ProducerFactory<UUID, D> updateProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> updateConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private <D extends Dto> ReplyingKafkaTemplate<UUID, D, IdDto> updateReplyingKafkaTemplate(KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer,
                                                                                              String kafkaServer) {
        return new ReplyingKafkaTemplate<>(updateProducerFactory(kafkaServer), updateRepliesContainer);
    }

    private KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer(ConsumerFactory<UUID, IdDto> updateReplyConsumerFactory,
                                                                              String kafkaGroupId,
                                                                              String consumerId) {
        String updateReplyTopicId = getUpdateReplyTopicId(consumerId, kafkaGroupId);
        ContainerProperties containerProperties = new ContainerProperties(updateReplyTopicId);
        return new KafkaMessageListenerContainer<>(updateReplyConsumerFactory, containerProperties);
    }

    private ConsumerFactory<UUID, IdDto> updateReplyConsumerFactory(String kafkaServer,
                                                                    String kafkaGroupId) {
        JsonDeserializer<IdDto> groupPhotoDtoJsonDeserializer = new JsonDeserializer<>();
        groupPhotoDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(updateConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                groupPhotoDtoJsonDeserializer);
    }

    public <D extends Dto> void configure(String kafkaServer,
                          String kafkaGroupId,
                          ConfigurableListableBeanFactory beanFactory,
                          ProducerRights<D> producerConfig) {
        if (!producerConfig.isUpdate()) {
            return;
        }
        ConsumerFactory<UUID, IdDto> updateReplyConsumerFactory = updateReplyConsumerFactory(kafkaServer, kafkaGroupId);
        String producerId = producerConfig.getProducerId();
        KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer =
                updateRepliesContainer(updateReplyConsumerFactory, kafkaGroupId, producerId);
        beanFactory.registerSingleton(producerId + "UpdateRepliesContainer", updateRepliesContainer);
        ReplyingKafkaTemplate<UUID, D, IdDto> updateReplyingKafkaTemplate = updateReplyingKafkaTemplate(updateRepliesContainer, kafkaServer);
        beanFactory.registerSingleton(producerId + "UpdateReplyingKafkaTemplate", updateReplyingKafkaTemplate);
        UpdateKafkaProducer<D> commonUpdateKafkaProducer =
                CommonUpdateKafkaProducer.create(updateReplyingKafkaTemplate, kafkaGroupId, producerId);
        beanFactory.registerSingleton(producerId + ProducerPostfix.UPDATE.getDescription(), commonUpdateKafkaProducer);
        producerConfig.getProducer().setUpdateKafkaProducer(commonUpdateKafkaProducer);
        log.info("Update producer for " + producerId + " successfully configured!");
    }

    private String getUpdateReplyTopicId(String consumerId, String kafkaGroupId) {
        return consumerId + TopicKafkaPostfix.UPDATE + ReplyKafkaProducer.KAFKA_REPLY_ID + kafkaGroupId;
    }
}
