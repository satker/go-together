package org.go.together.kafka.producer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.go.together.kafka.producer.impl.CommonUpdateKafkaProducer;
import org.go.together.kafka.producers.ReplyKafkaProducer;
import org.go.together.kafka.producers.crud.UpdateKafkaProducer;
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

public abstract class UpdateProducerKafkaConfig<D extends Dto> extends ValidateProducerKafkaConfig<D> {
    private ProducerFactory<UUID, D> updateProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private ReplyingKafkaTemplate<UUID, D, IdDto> updateReplyingKafkaTemplate(KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer,
                                                                              String kafkaServer) {
        return new ReplyingKafkaTemplate<>(updateProducerFactory(kafkaServer), updateRepliesContainer);
    }

    private KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer(ConsumerFactory<UUID, IdDto> updateReplyConsumerFactory,
                                                                              String kafkaGroupId) {
        ContainerProperties containerProperties = new ContainerProperties(getUpdateReplyTopicId() + kafkaGroupId);
        return new KafkaMessageListenerContainer<>(updateReplyConsumerFactory, containerProperties);
    }


    private Map<String, Object> updateConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);

        return props;
    }

    private ConsumerFactory<UUID, IdDto> updateReplyConsumerFactory(String kafkaServer,
                                                                    String kafkaGroupId) {
        JsonDeserializer<IdDto> groupPhotoDtoJsonDeserializer = new JsonDeserializer<>();
        groupPhotoDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(updateConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                groupPhotoDtoJsonDeserializer);
    }

    protected void updateProducerBeanFactoryPostProcessor(String kafkaServer,
                                                          String kafkaGroupId,
                                                          ConfigurableListableBeanFactory beanFactory) {
        ConsumerFactory<UUID, IdDto> updateReplyConsumerFactory = updateReplyConsumerFactory(kafkaServer, kafkaGroupId);
        KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer = updateRepliesContainer(updateReplyConsumerFactory, kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + "UpdateRepliesContainer", updateRepliesContainer);
        ReplyingKafkaTemplate<UUID, D, IdDto> updateReplyingKafkaTemplate = updateReplyingKafkaTemplate(updateRepliesContainer, kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "UpdateReplyingKafkaTemplate", updateReplyingKafkaTemplate);
        UpdateKafkaProducer<D> commonUpdateKafkaProducer = CommonUpdateKafkaProducer.create(updateReplyingKafkaTemplate, kafkaGroupId,
                getConsumerId());
        beanFactory.registerSingleton(getConsumerId() + ProducerPostfix.UPDATE.getDescription(), commonUpdateKafkaProducer);
    }

    private String getUpdateReplyTopicId() {
        return getConsumerId() + TopicKafkaPostfix.UPDATE.getDescription() + ReplyKafkaProducer.KAFKA_REPLY_ID;
    }
}
