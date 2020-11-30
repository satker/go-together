package org.go.together.kafka.config.producers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.kafka.enums.ProducerPostfix;
import org.go.together.kafka.impl.producers.CommonFindKafkaProducer;
import org.go.together.kafka.producers.crud.FindKafkaProducer;
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

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.go.together.enums.TopicKafkaPostfix.FIND;
import static org.go.together.kafka.producers.ReplyKafkaProducer.KAFKA_REPLY_ID;

public abstract class FindProducerKafkaConfig<D extends Dto> extends DeleteProducerKafkaConfig<D> {
    private ProducerFactory<UUID, FormDto> findProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> findConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
    }

    private ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> findReplyingKafkaTemplate(String kafkaServer,
                                                                                                KafkaMessageListenerContainer<UUID, ResponseDto<Object>> repliesContainer) {
        return new ReplyingKafkaTemplate<>(findProducerFactory(kafkaServer), repliesContainer);
    }

    private KafkaMessageListenerContainer<UUID, ResponseDto<Object>> findRepliesContainer(ConsumerFactory<UUID, ResponseDto<Object>> readReplyConsumerFactory,
                                                                                          String kafkaGroupId) {
        String replyTopic = getReplyTopicId() + kafkaGroupId;
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

    protected void findProducerBeanFactoryPostProcessor(String kafkaServer,
                                                        String kafkaGroupId,
                                                        ConfigurableListableBeanFactory beanFactory) {
        ConsumerFactory<UUID, ResponseDto<Object>> consumerFactory = findReplyConsumerFactory(kafkaServer, kafkaGroupId);
        KafkaMessageListenerContainer<UUID, ResponseDto<Object>> kafkaMessageListenerContainer = findRepliesContainer(consumerFactory, kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + "FindRepliesContainer", kafkaMessageListenerContainer);
        ReplyingKafkaTemplate<UUID, FormDto, ResponseDto<Object>> replyingKafkaTemplate = findReplyingKafkaTemplate(kafkaServer, kafkaMessageListenerContainer);
        beanFactory.registerSingleton(getConsumerId() + "FindReplyingKafkaTemplate", replyingKafkaTemplate);
        FindKafkaProducer<D> commonFindKafkaProducer = CommonFindKafkaProducer.create(replyingKafkaTemplate, getConsumerId(), kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + ProducerPostfix.FIND.getDescription(), commonFindKafkaProducer);
    }

    private String getReplyTopicId() {
        return getConsumerId() + FIND.getDescription() + KAFKA_REPLY_ID;
    }
}
