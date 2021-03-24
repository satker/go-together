package org.go.together.kafka.consumer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.go.together.dto.FormDto;
import org.go.together.dto.ResponseDto;
import org.go.together.kafka.consumer.config.interfaces.CustomConsumerConfig;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

import static org.go.together.interfaces.TopicKafkaPostfix.FIND;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

public abstract class FindConsumerKafkaConfig implements CustomConsumerConfig {
    private Map<String, Object> getFindProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private ProducerFactory<Long, ResponseDto<Object>> findReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getFindProducerConfigs(kafkaServer));
    }

    private KafkaTemplate<Long, ResponseDto<Object>> findKafkaTemplate(ProducerFactory<Long, ResponseDto<Object>> findReplyProducerFactory) {
        return new KafkaTemplate<>(findReplyProducerFactory);
    }

    private ConsumerFactory<Long, FormDto> findConsumerFactory(String kafkaServer, String kafkaGroupId) {
        JsonDeserializer<FormDto> formDtoJsonDeserializer = new JsonDeserializer<>();
        formDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(getConsumerConfigs(kafkaServer, kafkaGroupId), new LongDeserializer(),
                formDtoJsonDeserializer);
    }

    private ConcurrentKafkaListenerContainerFactory<Long, FormDto> findListenerContainerFactory(ConsumerFactory<Long, FormDto> findConsumerFactory,
                                                                                                KafkaTemplate<Long, ResponseDto<Object>> findKafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<Long, FormDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(findConsumerFactory);
        factory.setReplyTemplate(findKafkaTemplate);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(500L, 2L)));
        return factory;
    }

    public void findConsumerBeanFactoryPostProcessor(String kafkaServer, String kafkaGroupId, ConfigurableListableBeanFactory beanFactory) {
        ProducerFactory<Long, ResponseDto<Object>> replyProducerFactory = findReplyProducerFactory(kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "FindReplyProducerFactory", replyProducerFactory);
        KafkaTemplate<Long, ResponseDto<Object>> kafkaTemplate = findKafkaTemplate(replyProducerFactory);
        beanFactory.registerSingleton(getConsumerId() + "FindKafkaTemplate", kafkaTemplate);
        ConsumerFactory<Long, FormDto> consumerFactory = findConsumerFactory(kafkaServer, kafkaGroupId);
        ConcurrentKafkaListenerContainerFactory<Long, FormDto> listenerContainerFactory =
                findListenerContainerFactory(consumerFactory, kafkaTemplate);
        beanFactory.registerSingleton(getConsumerId() + FIND + LISTENER_FACTORY, listenerContainerFactory);
    }
}
