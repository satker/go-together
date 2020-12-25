package org.go.together.kafka.consumer.config.crud;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
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
import java.util.UUID;

import static org.go.together.enums.TopicKafkaPostfix.CHANGE;
import static org.go.together.kafka.consumer.constants.ConsumerBeanConfigName.LISTENER_FACTORY;

public abstract class ChangeConsumerKafkaConfig<D extends Dto> extends ValidateConsumerKafkaConfig<D> {
    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId
        );
    }

    private Map<String, Object> getChangeProducerConfigs(String kafkaServer) {
        return Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );
    }

    private ConsumerFactory<UUID, D> changeConsumerFactory(String kafkaServer, String kafkaGroupId) {
        JsonDeserializer<D> groupPhotoDtoJsonDeserializer = new JsonDeserializer<>();
        groupPhotoDtoJsonDeserializer.addTrustedPackages("org.go.together.dto");
        return new DefaultKafkaConsumerFactory<>(getConsumerConfigs(kafkaServer, kafkaGroupId), new UUIDDeserializer(),
                groupPhotoDtoJsonDeserializer);
    }

    private ConcurrentKafkaListenerContainerFactory<UUID, D> changeListenerContainerFactory(ConsumerFactory<UUID, D> changeConsumerFactory,
                                                                                            KafkaTemplate<UUID, IdDto> changeKafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<UUID, D> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(changeConsumerFactory);
        factory.setReplyTemplate(changeKafkaTemplate);
        factory.setErrorHandler(new SeekToCurrentErrorHandler(new FixedBackOff(500L, 2L)));
        return factory;
    }

    public ProducerFactory<UUID, IdDto> changeReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getChangeProducerConfigs(kafkaServer));
    }

    public KafkaTemplate<UUID, IdDto> changeKafkaTemplate(ProducerFactory<UUID, IdDto> changeReplyProducerFactory) {
        return new KafkaTemplate<>(changeReplyProducerFactory);
    }

    protected void changeConsumerBeanFactoryPostProcessor(String kafkaServer,
                                                          String kafkaGroupId,
                                                          ConfigurableListableBeanFactory beanFactory) {
        ProducerFactory<UUID, IdDto> producerFactory = changeReplyProducerFactory(kafkaServer);
        beanFactory.registerSingleton(getConsumerId() + "ChangeReplyProducerFactory", producerFactory);
        KafkaTemplate<UUID, IdDto> kafkaTemplate = changeKafkaTemplate(producerFactory);
        beanFactory.registerSingleton(getConsumerId() + "ChangeKafkaTemplate", kafkaTemplate);
        ConsumerFactory<UUID, D> consumerFactory = changeConsumerFactory(kafkaServer, kafkaGroupId);
        beanFactory.registerSingleton(getConsumerId() + CHANGE + LISTENER_FACTORY,
                changeListenerContainerFactory(consumerFactory, kafkaTemplate));
    }
}
