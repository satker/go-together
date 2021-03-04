package org.go.together.kafka.producer.config.crud;

import brave.Tracer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producer.beanpostprocessor.ProducerRights;
import org.go.together.kafka.producer.config.interfaces.KafkaProducerConfigurator;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.go.together.kafka.producer.impl.CommonFindKafkaProducer;
import org.go.together.kafka.producers.ReplyKafkaProducer;
import org.go.together.kafka.producers.crud.FindKafkaProducer;
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
public class FindProducerKafkaConfig implements KafkaProducerConfigurator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ProducerFactory<UUID, FormDto> findProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> findConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> findReplyingKafkaTemplate(String kafkaServer,
                                                                                                KafkaMessageListenerContainer<UUID, ResponseDto<Object>> repliesContainer) {
        return new ReplyingKafkaTemplate<>(findProducerFactory(kafkaServer), repliesContainer);
    }

    private KafkaMessageListenerContainer<UUID, ResponseDto<Object>> findRepliesContainer(ConsumerFactory<UUID, ResponseDto<Object>> readReplyConsumerFactory,
                                                                                          String kafkaGroupId,
                                                                                          String consumerId) {
        String replyTopic = getReplyTopicId(consumerId, kafkaGroupId);
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(readReplyConsumerFactory, containerProperties);
    }

    private ConsumerFactory<UUID, ResponseDto<Object>> findReplyConsumerFactory(String kafkaServer, String kafkaGroupId) {
        JsonDeserializer<ResponseDto<Object>> resultJsonDeserializer = new JsonDeserializer<>();
        resultJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(findConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                resultJsonDeserializer);
    }

    public <D extends Dto> void configure(String kafkaServer,
                                          String kafkaGroupId,
                                          ConfigurableListableBeanFactory beanFactory,
                                          ProducerRights<D> producerConfig,
                                          Tracer tracer) {
        if (!producerConfig.isFind()) {
            return;
        }
        ConsumerFactory<UUID, ResponseDto<Object>> consumerFactory = findReplyConsumerFactory(kafkaServer, kafkaGroupId);
        String producerId = producerConfig.getProducerId();
        KafkaMessageListenerContainer<UUID, ResponseDto<Object>> kafkaMessageListenerContainer =
                findRepliesContainer(consumerFactory, kafkaGroupId, producerId);
        beanFactory.registerSingleton(producerId + "FindRepliesContainer", kafkaMessageListenerContainer);
        ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> replyingKafkaTemplate = findReplyingKafkaTemplate(kafkaServer, kafkaMessageListenerContainer);
        beanFactory.registerSingleton(producerId + "FindReplyingKafkaTemplate", replyingKafkaTemplate);
        FindKafkaProducer<D> commonFindKafkaProducer = CommonFindKafkaProducer.create(replyingKafkaTemplate, producerId, kafkaGroupId, tracer);
        beanFactory.registerSingleton(producerId + ProducerPostfix.FIND.getDescription(), commonFindKafkaProducer);
        producerConfig.getProducer().setFindKafkaProducer(commonFindKafkaProducer);
        log.info("Find producer for " + producerId + " successfully configured!");
    }

    private String getReplyTopicId(String consumerId, String kafkaGroupId) {
        return consumerId + TopicKafkaPostfix.FIND + ReplyKafkaProducer.KAFKA_REPLY_ID + kafkaGroupId;
    }
}
