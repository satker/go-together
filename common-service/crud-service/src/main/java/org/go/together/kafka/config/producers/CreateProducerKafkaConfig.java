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

public abstract class CreateProducerKafkaConfig<D extends Dto> {
    public ProducerFactory<UUID, D> createProducerFactory(String kafkaServer) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    public ReplyingKafkaTemplate<UUID, D, IdDto> createReplyingKafkaTemplate(KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer,
                                                                             String kafkaServer) {
        return new ReplyingKafkaTemplate<>(createProducerFactory(kafkaServer), createRepliesContainer);
    }

    public KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer(@Qualifier("createReplyConsumerFactory") ConsumerFactory<UUID, IdDto> createReplyConsumerFactory) {
        ContainerProperties containerProperties = new ContainerProperties(getCreateReplyTopicId());
        return new KafkaMessageListenerContainer<>(createReplyConsumerFactory, containerProperties);
    }

    @Bean
    public BeanFactoryPostProcessor createProducerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                           @Qualifier("createReplyConsumerFactory") ConsumerFactory<UUID, IdDto> changeReplyConsumerFactory) {
        return beanFactory -> {
            KafkaMessageListenerContainer<UUID, IdDto> createRepliesContainer = createRepliesContainer(changeReplyConsumerFactory);
            beanFactory.registerSingleton(getConsumerId() + "CreateRepliesContainer", createRepliesContainer);
            ReplyingKafkaTemplate<UUID, D, IdDto> createReplyingKafkaTemplate = createReplyingKafkaTemplate(createRepliesContainer, kafkaServer);
            beanFactory.registerSingleton(getConsumerId() + "CreateReplyingKafkaTemplate", createReplyingKafkaTemplate);
        };
    }

    public String getCreateReplyTopicId() {
        return getConsumerId() + TopicKafkaPostfix.CREATE.getDescription() + KAFKA_REPLY_ID;
    }

    public abstract String getConsumerId();
}
