package org.go.together.kafka.config.producers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.kafka.impl.producers.CommonReadKafkaProducer;
import org.go.together.kafka.producers.crud.ReadKafkaProducer;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.go.together.enums.TopicKafkaPostfix.READ;
import static org.go.together.kafka.producers.ReplyKafkaProducer.KAFKA_REPLY_ID;

public abstract class ReadProducerKafkaConfig<D extends Dto> extends UpdateProducerKafkaConfig<D> {
    private ProducerFactory<UUID, UUID> readProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> readConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
    }

    private ReplyingKafkaTemplate<UUID, UUID, D> readReplyingKafkaTemplate(String kafkaServer,
                                                                           KafkaMessageListenerContainer<UUID, D> repliesContainer) {
        return new ReplyingKafkaTemplate<>(readProducerFactory(kafkaServer), repliesContainer);
    }

    private KafkaMessageListenerContainer<UUID, D> readRepliesContainer(ConsumerFactory<UUID, D> readReplyConsumerFactory,
                                                                        String kafkaGroupId) {
        String replyTopic = getReplyTopicId() + kafkaGroupId;
        ContainerProperties containerProperties = new ContainerProperties(replyTopic);
        return new KafkaMessageListenerContainer<>(readReplyConsumerFactory, containerProperties);
    }

    private ConsumerFactory<UUID, D> readReplyConsumerFactory(String kafkaServer, String kafkaGroupId) {
        JsonDeserializer<D> readDtoJsonDeserializer = new JsonDeserializer<>();
        readDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(readConsumerConfigs(kafkaServer, kafkaGroupId), new UUIDDeserializer(),
                readDtoJsonDeserializer);
    }

    protected void readProducerBeanFactoryPostProcessor(String kafkaServer,
                                                        String kafkaGroupId,
                                                        ConfigurableListableBeanFactory beanFactory) {
        ConsumerFactory<UUID, D> consumerFactory = readReplyConsumerFactory(kafkaServer, kafkaGroupId);
        KafkaMessageListenerContainer<UUID, D> kafkaMessageListenerContainer = readRepliesContainer(consumerFactory, kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + "ReadRepliesContainer", kafkaMessageListenerContainer);
        ReplyingKafkaTemplate<UUID, UUID, D> replyingKafkaTemplate = readReplyingKafkaTemplate(kafkaServer, kafkaMessageListenerContainer);
        beanFactory.registerSingleton(getConsumerId() + "ReadReplyingKafkaTemplate", replyingKafkaTemplate);
        ReadKafkaProducer<D> commonReadKafkaProducer = CommonReadKafkaProducer.create(replyingKafkaTemplate, kafkaGroupId, getConsumerId());
        beanFactory.registerSingleton(getConsumerId() + "ReadKafkaProducer", commonReadKafkaProducer);
    }

    private String getReplyTopicId() {
        return getConsumerId() + READ.getDescription() + KAFKA_REPLY_ID;
    }
}
