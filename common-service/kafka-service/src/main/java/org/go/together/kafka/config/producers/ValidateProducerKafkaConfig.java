package org.go.together.kafka.config.producers;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.impl.producers.CommonValidateKafkaProducer;
import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.go.together.kafka.interfaces.producers.ReplyKafkaProducer.KAFKA_REPLY_ID;

public abstract class ValidateProducerKafkaConfig<D extends Dto> extends FindProducerKafkaConfig<D> {
    private ProducerFactory<UUID, D> validateProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
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


    @Bean
    public BeanFactoryPostProcessor validateProducerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                             @Value("${kafka.groupId}") String kafkaGroupId,
                                                                             ConsumerFactory<UUID, ValidationMessageDto> changeReplyConsumerFactory) {
        return beanFactory -> {
            KafkaMessageListenerContainer<UUID, ValidationMessageDto> updateRepliesContainer = validateRepliesContainer(changeReplyConsumerFactory, kafkaGroupId);
            beanFactory.registerSingleton(getConsumerId() + "ValidateRepliesContainer", updateRepliesContainer);
            ReplyingKafkaTemplate<UUID, D, ValidationMessageDto> updateReplyingKafkaTemplate = validateReplyingKafkaTemplate(updateRepliesContainer, kafkaServer);
            beanFactory.registerSingleton(getConsumerId() + "ValidateReplyingKafkaTemplate", updateReplyingKafkaTemplate);
            CommonValidateKafkaProducer<D> commonValidateKafkaProducer = new CommonValidateKafkaProducer<>(updateReplyingKafkaTemplate, kafkaGroupId) {
                @Override
                public String getTopicId() {
                    return getConsumerId();
                }
            };
            beanFactory.registerSingleton(getConsumerId() + "ValidateKafkaProducer", commonValidateKafkaProducer);
        };
    }

    private String getValidateReplyTopicId() {
        return getConsumerId() + TopicKafkaPostfix.VALIDATE.getDescription() + KAFKA_REPLY_ID;
    }
}
