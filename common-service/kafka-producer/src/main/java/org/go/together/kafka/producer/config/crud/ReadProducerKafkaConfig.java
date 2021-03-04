package org.go.together.kafka.producer.config.crud;

import brave.Tracer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producer.beanpostprocessor.ProducerRights;
import org.go.together.kafka.producer.config.interfaces.KafkaProducerConfigurator;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.go.together.kafka.producer.impl.CommonReadKafkaProducer;
import org.go.together.kafka.producers.ReplyKafkaProducer;
import org.go.together.kafka.producers.crud.ReadKafkaProducer;
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
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

@Component
public class ReadProducerKafkaConfig implements KafkaProducerConfigurator  {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private ProducerFactory<UUID, UUID> readProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> readConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private <D extends Dto> ReplyingKafkaTemplate<UUID, UUID, D> readReplyingKafkaTemplate(String kafkaServer,
                                                                           KafkaMessageListenerContainer<UUID, D> repliesContainer) {
        return new ReplyingKafkaTemplate<>(readProducerFactory(kafkaServer), repliesContainer);
    }

    private <D extends Dto> KafkaMessageListenerContainer<UUID, D> readRepliesContainer(ConsumerFactory<UUID, D> readReplyConsumerFactory,
                                                                        String kafkaGroupId,
                                                                        String consumerId) {
        String replyTopic = getReplyTopicId(consumerId, kafkaGroupId);
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(readReplyConsumerFactory, containerProperties);
    }

    private <D extends Dto> ConsumerFactory<UUID, D> readReplyConsumerFactory(String kafkaServer, String kafkaGroupId) {
        JsonDeserializer<D> readDtoJsonDeserializer = new JsonDeserializer<>();
        readDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(readConsumerConfigs(kafkaServer, kafkaGroupId), new UUIDDeserializer(),
                readDtoJsonDeserializer);
    }

    public <D extends Dto> void configure(String kafkaServer,
                                          String kafkaGroupId,
                                          ConfigurableListableBeanFactory beanFactory,
                                          ProducerRights<D> producerConfig,
                                          Tracer tracer) {
        if (!producerConfig.isRead()) {
            return;
        }
        ConsumerFactory<UUID, D> consumerFactory = readReplyConsumerFactory(kafkaServer, kafkaGroupId);
        String producerId = producerConfig.getProducerId();
        KafkaMessageListenerContainer<UUID, D> kafkaMessageListenerContainer = readRepliesContainer(consumerFactory, kafkaGroupId, producerId);
        beanFactory.registerSingleton(producerId + "ReadRepliesContainer", kafkaMessageListenerContainer);
        ReplyingKafkaTemplate<UUID, UUID, D> replyingKafkaTemplate = readReplyingKafkaTemplate(kafkaServer, kafkaMessageListenerContainer);
        beanFactory.registerSingleton(producerId + "ReadReplyingKafkaTemplate", replyingKafkaTemplate);
        ReadKafkaProducer<D> commonReadKafkaProducer = CommonReadKafkaProducer.create(replyingKafkaTemplate, kafkaGroupId, producerId, tracer);
        beanFactory.registerSingleton(producerId + ProducerPostfix.READ.getDescription(), commonReadKafkaProducer);
        producerConfig.getProducer().setReadKafkaProducer(commonReadKafkaProducer);
        log.info("Read producer for " + producerId + " successfully configured!");
    }

    private String getReplyTopicId(String consumerId, String kafkaGroupId) {
        return consumerId + TopicKafkaPostfix.READ + ReplyKafkaProducer.KAFKA_REPLY_ID + kafkaGroupId;
    }
}
