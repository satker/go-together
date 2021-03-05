package org.go.together.kafka.consumer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.apache.kafka.common.serialization.LongSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.Map;

import static org.go.together.enums.TopicKafkaPostfix.CHANGE;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

public abstract class ChangeConsumerKafkaConfig<D extends Dto> extends ValidateConsumerKafkaConfig<D> {
    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private Map<String, Object> getChangeProducerConfigs(String kafkaServer) {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    private ConsumerFactory<Long, D> changeConsumerFactory(String kafkaServer, String kafkaGroupId) {
        JsonDeserializer<D> groupPhotoDtoJsonDeserializer = new JsonDeserializer<>();
        groupPhotoDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(getConsumerConfigs(kafkaServer, kafkaGroupId), new LongDeserializer(),
                groupPhotoDtoJsonDeserializer);
    }

    private ConcurrentKafkaListenerContainerFactory<Long, D> changeListenerContainerFactory(ConsumerFactory<Long, D> changeConsumerFactory,
                                                                                            KafkaTemplate<Long, IdDto> changeKafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<Long, D> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(changeConsumerFactory);
        factory.setReplyTemplate(changeKafkaTemplate);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(500L, 2L)));
        return factory;
    }

    public ProducerFactory<Long, IdDto> changeReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getChangeProducerConfigs(kafkaServer));
    }

    public KafkaTemplate<Long, IdDto> changeKafkaTemplate(ProducerFactory<Long, IdDto> changeReplyProducerFactory) {
        return new KafkaTemplate<>(changeReplyProducerFactory);
    }

    protected void changeConsumerBeanFactoryPostProcessor(String kafkaServer,
                                                          String kafkaGroupId,
                                                          ConfigurableListableBeanFactory beanFactory) {
        ProducerFactory<Long, IdDto> producerFactory = changeReplyProducerFactory(kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "ChangeReplyProducerFactory", producerFactory);
        KafkaTemplate<Long, IdDto> kafkaTemplate = changeKafkaTemplate(producerFactory);
        beanFactory.registerSingleton(getConsumerId() + "ChangeKafkaTemplate", kafkaTemplate);
        ConsumerFactory<Long, D> consumerFactory = changeConsumerFactory(kafkaServer, kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + CHANGE + LISTENER_FACTORY,
                changeListenerContainerFactory(consumerFactory, kafkaTemplate));
    }
}
