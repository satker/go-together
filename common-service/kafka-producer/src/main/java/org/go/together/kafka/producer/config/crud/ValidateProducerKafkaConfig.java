package org.go.together.kafka.producer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.enums.TopicKafkaPostfix;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.go.together.kafka.producer.impl.CommonValidateKafkaProducer;
import org.go.together.kafka.producers.ReplyKafkaProducer;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;
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

public abstract class ValidateProducerKafkaConfig<D extends Dto> extends FindProducerKafkaConfig<D> {
    private ProducerFactory<UUID, D> validateProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> validateConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);

        return props;
    }

    private ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> validateReplyingKafkaTemplate(KafkaMessageListenerContainer<UUID, ValidationMessageDto> validateRepliesContainer,
                                                                                               String kafkaServer) {
        return new ReplyingKafkaTemplate<>(validateProducerFactory(kafkaServer), validateRepliesContainer);
    }

    private KafkaMessageListenerContainer<UUID, ValidationMessageDto> validateRepliesContainer(ConsumerFactory<UUID, ValidationMessageDto> validateReplyConsumerFactory,
                                                                                               String kafkaGroupId) {
        ContainerProperties containerProperties = new ContainerProperties(getValidateReplyTopicId() + kafkaGroupId);
        return new KafkaMessageListenerContainer<>(validateReplyConsumerFactory, containerProperties);
    }

    private ConsumerFactory<UUID, ValidationMessageDto> validateReplyConsumerFactory(String kafkaServer,
                                                                                     String kafkaGroupId) {
        JsonDeserializer<ValidationMessageDto> validationMessageDtoJsonDeserializer = new JsonDeserializer<>();
        validationMessageDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(validateConsumerConfigs(kafkaServer, kafkaGroupId),
                new UUIDDeserializer(),
                validationMessageDtoJsonDeserializer);
    }

    protected void validateProducerBeanFactoryPostProcessor(String kafkaServer,
                                                            String kafkaGroupId,
                                                            ConfigurableListableBeanFactory beanFactory) {
        ConsumerFactory<UUID, ValidationMessageDto> replyConsumerFactory = validateReplyConsumerFactory(kafkaServer, kafkaGroupId);
        KafkaMessageListenerContainer<UUID, ValidationMessageDto> updateRepliesContainer = validateRepliesContainer(replyConsumerFactory, kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + "ValidateRepliesContainer", updateRepliesContainer);
        ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> updateReplyingKafkaTemplate = validateReplyingKafkaTemplate(updateRepliesContainer, kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "ValidateReplyingKafkaTemplate", updateReplyingKafkaTemplate);
        ValidateKafkaProducer<D> commonValidateKafkaProducer = CommonValidateKafkaProducer.create(updateReplyingKafkaTemplate, kafkaGroupId,
                getConsumerId());
        beanFactory.registerSingleton(getConsumerId() + ProducerPostfix.VALIDATE.getDescription(), commonValidateKafkaProducer);
    }

    private String getValidateReplyTopicId() {
        return getConsumerId() + TopicKafkaPostfix.VALIDATE + ReplyKafkaProducer.KAFKA_REPLY_ID;
    }
}
