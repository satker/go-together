package org.go.together.kafka.config.producers;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.impl.producers.CommonCreateKafkaProducer;
import org.go.together.kafka.producers.crud.CreateKafkaProducer;
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

import static org.go.together.kafka.producers.ReplyKafkaProducer.KAFKA_REPLY_ID;


public abstract class CreateProducerKafkaConfig<D extends Dto> extends ReadProducerKafkaConfig<D> {
    private ProducerFactory<UUID, D> createProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private ReplyingKafkaTemplate<UUID, D, IdDto> createReplyingKafkaTemplate(KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer,
                                                                              String kafkaServer) {
        return new ReplyingKafkaTemplate<>(createProducerFactory(kafkaServer), createRepliesContainer);
    }

    private KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer(ConsumerFactory<UUID, IdDto> createReplyConsumerFactory,
                                                                              String kafkaGroupId) {
        ContainerProperties containerProperties = new ContainerProperties(getCreateReplyTopicId() + kafkaGroupId);
        return new KafkaMessageListenerContainer<>(createReplyConsumerFactory, containerProperties);
    }

    private Map<String, Object> createConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);

        return props;
    }

    private ConsumerFactory<UUID, IdDto> createReplyConsumerFactory(String kafkaServer,
                                                                    String kafkaGroupId) {
        JsonDeserializer<IdDto> groupPhotoDtoJsonDeserializer = new JsonDeserializer<>();
        groupPhotoDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(createConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                groupPhotoDtoJsonDeserializer);
    }

    protected void createProducerBeanFactoryPostProcessor(String kafkaServer,
                                                          String kafkaGroupId,
                                                          ConfigurableListableBeanFactory beanFactory) {
        ConsumerFactory<UUID, IdDto> replyConsumerFactory = createReplyConsumerFactory(kafkaServer, kafkaGroupId);
        KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer = createRepliesContainer(replyConsumerFactory, kafkaGroupId);
        ReplyingKafkaTemplate<UUID, D, IdDto> createReplyingKafkaTemplate = createReplyingKafkaTemplate(createRepliesContainer, kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "CreateReplyingKafkaTemplate", createReplyingKafkaTemplate);
        CreateKafkaProducer<D> commonCreateKafkaProducer =
                this.new CustomCreateProducer(createReplyingKafkaTemplate, kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + "CreateKafkaProducer", commonCreateKafkaProducer);
    }

    private class CustomCreateProducer extends CommonCreateKafkaProducer<D> {
        public CustomCreateProducer(ReplyingKafkaTemplate<UUID, D, IdDto> kafkaTemplate, String groupId) {
            super(kafkaTemplate, groupId);
        }

        @Override
        public String getTopicId() {
            return getConsumerId();
        }
    }

    private String getCreateReplyTopicId() {
        return getConsumerId() + TopicKafkaPostfix.CREATE.getDescription() + KAFKA_REPLY_ID;
    }
}
