package org.go.together.kafka.config.producers;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.interfaces.TopicKafkaPostfix;
import org.springframework.beans.factory.annotation.Qualifier;
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

public abstract class UpdateProducerKafkaConfig<D extends Dto> {
    public ProducerFactory<UUID, D> updateProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public ReplyingKafkaTemplate<UUID, D, IdDto> updateReplyingKafkaTemplate(KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer,
                                                                             String kafkaServer) {
        return new ReplyingKafkaTemplate<>(updateProducerFactory(kafkaServer), updateRepliesContainer);
    }

    public KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer(@Qualifier("updateReplyConsumerFactory")
                                                                                     ConsumerFactory<UUID, IdDto> changeReplyConsumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties(getUpdateReplyTopicId());
        return new KafkaMessageListenerContainer<>(changeReplyConsumerFactory, containerProperties);
    }


    @Bean
    public BeanFactoryPostProcessor changeProducerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                           @Qualifier("updateReplyConsumerFactory")
                                                                                   ConsumerFactory<UUID, IdDto> changeReplyConsumerFactory) {
        return beanFactory -> {
            KafkaMessageListenerContainer<UUID, IdDto> updateRepliesContainer = updateRepliesContainer(changeReplyConsumerFactory);
            beanFactory.registerSingleton(getConsumerId() + "UpdateRepliesContainer", updateRepliesContainer);
            ReplyingKafkaTemplate<UUID, D, IdDto> updateReplyingKafkaTemplate = updateReplyingKafkaTemplate(updateRepliesContainer, kafkaServer);
            beanFactory.registerSingleton(getConsumerId() + "UpdateReplyingKafkaTemplate", updateReplyingKafkaTemplate);
        };
    }

    public String getUpdateReplyTopicId() {
        return getConsumerId() + TopicKafkaPostfix.UPDATE.getDescription() + KAFKA_REPLY_ID;
    }

    public abstract String getConsumerId();
}
