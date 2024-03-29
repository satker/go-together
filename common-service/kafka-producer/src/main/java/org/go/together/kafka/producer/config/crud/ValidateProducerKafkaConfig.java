package org.go.together.kafka.producer.config.crud;

import brave.Tracer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.interfaces.TopicKafkaPostfix;
import org.go.together.kafka.producer.beanpostprocessor.ProducerRights;
import org.go.together.kafka.producer.config.interfaces.KafkaProducerConfigurator;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.go.together.kafka.producer.impl.CommonValidateKafkaProducer;
import org.go.together.kafka.producers.ReplyKafkaProducer;
import org.go.together.kafka.producers.crud.ValidateKafkaProducer;
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

@Component
public class ValidateProducerKafkaConfig implements KafkaProducerConfigurator {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private <D extends Dto> ProducerFactory<Long, D> validateProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    private Map<String, Object> validateConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private <D extends Dto> ReplyingKafkaTemplate<Long, D, ValidationMessageDto> validateReplyingKafkaTemplate(KafkaMessageListenerContainer<Long, ValidationMessageDto> validateRepliesContainer,
                                                                                                               String kafkaServer) {
        return new ReplyingKafkaTemplate<>(validateProducerFactory(kafkaServer), validateRepliesContainer);
    }

    private KafkaMessageListenerContainer<Long, ValidationMessageDto> validateRepliesContainer(ConsumerFactory<Long, ValidationMessageDto> validateReplyConsumerFactory,
                                                                                               String kafkaGroupId,
                                                                                               String consumerId) {
        String validateReplyTopicId = getValidateReplyTopicId(consumerId, kafkaGroupId);
        ContainerProperties containerProperties = new ContainerProperties(validateReplyTopicId);
        return new KafkaMessageListenerContainer<>(validateReplyConsumerFactory, containerProperties);
    }

    private ConsumerFactory<Long, ValidationMessageDto> validateReplyConsumerFactory(String kafkaServer,
                                                                                     String kafkaGroupId) {
        JsonDeserializer<ValidationMessageDto> validationMessageDtoJsonDeserializer = new JsonDeserializer<>();
        validationMessageDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(validateConsumerConfigs(kafkaServer, kafkaGroupId),
                new LongDeserializer(),
                validationMessageDtoJsonDeserializer);
    }

    public <D extends Dto> void configure(String kafkaServer,
                                          String kafkaGroupId,
                                          ConfigurableListableBeanFactory beanFactory,
                                          ProducerRights<D> producerConfig,
                                          Tracer tracer) {
        if (!producerConfig.isValidate()) {
            return;
        }
        ConsumerFactory<Long, ValidationMessageDto> replyConsumerFactory = validateReplyConsumerFactory(kafkaServer, kafkaGroupId);
        String producerId = producerConfig.getProducerId();
        KafkaMessageListenerContainer<Long, ValidationMessageDto> updateRepliesContainer =
                validateRepliesContainer(replyConsumerFactory, kafkaGroupId, producerId);
        beanFactory.registerSingleton(producerId + "ValidateRepliesContainer", updateRepliesContainer);
        ReplyingKafkaTemplate<Long, D, ValidationMessageDto> updateReplyingKafkaTemplate =
                validateReplyingKafkaTemplate(updateRepliesContainer, kafkaServer);
        beanFactory.registerSingleton(producerId + "ValidateReplyingKafkaTemplate", updateReplyingKafkaTemplate);
        ValidateKafkaProducer<D> commonValidateKafkaProducer =
                CommonValidateKafkaProducer.create(updateReplyingKafkaTemplate, kafkaGroupId, producerId, tracer);
        beanFactory.registerSingleton(producerId + ProducerPostfix.VALIDATE.getDescription(), commonValidateKafkaProducer);
        producerConfig.getProducer().setValidateKafkaProducers(commonValidateKafkaProducer);
        log.info("Validation producer for " + producerId + " successfully configured!");
    }

    private String getValidateReplyTopicId(String consumerId, String kafkaGroupId) {
        return consumerId + TopicKafkaPostfix.VALIDATE + ReplyKafkaProducer.KAFKA_REPLY_ID + kafkaGroupId;
    }
}
