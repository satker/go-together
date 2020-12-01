package org.go.together.kafka.consumer.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ChangeConsumerKafkaConfig<D extends Dto> extends ValidateConsumerKafkaConfig<D> {
    private Map<String, Object> getConsumerConfigs(String kafkaServer, String kafkaGroupId) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        return props;
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
        return factory;
    }

    private Map<String, Object> getChangeProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
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
        beanFactory.registerSingleton(getConsumerId() + "ChangeListenerContainerFactory",
                changeListenerContainerFactory(consumerFactory, kafkaTemplate));
    }
}
