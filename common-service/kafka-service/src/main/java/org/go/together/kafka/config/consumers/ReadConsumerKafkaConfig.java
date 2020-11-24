package org.go.together.kafka.config.consumers;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.go.together.dto.Dto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ReadConsumerKafkaConfig<D extends Dto> extends ChangeConsumerKafkaConfig<D> {
    private Map<String, Object> getProducerConfigs(String kafkaServer) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, "15728640");
        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, "snappy");
        return props;
    }

    private ConcurrentKafkaListenerContainerFactory<UUID, UUID> readListenerContainerFactory(ConsumerFactory<UUID, UUID> readConsumerFactory,
                                                                                             KafkaTemplate<UUID, D> kafkaTemplate) {
        ConcurrentKafkaListenerContainerFactory<UUID, UUID> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(readConsumerFactory);
        factory.setReplyTemplate(kafkaTemplate);
        return factory;
    }

    @Bean
    public BeanFactoryPostProcessor readConsumerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                         @Qualifier("readConsumerFactory") ConsumerFactory<UUID, UUID> readConsumerFactory) {
        return beanFactory -> {
            ProducerFactory<UUID, D> producerFactory = readReplyProducerFactory(kafkaServer);
            beanFactory.registerSingleton(getConsumerId() + "ReadReplyProducerFactory", producerFactory);
            KafkaTemplate<UUID, D> kafkaTemplate = readKafkaTemplate(producerFactory);
            beanFactory.registerSingleton(getConsumerId() + "ReadKafkaTemplate", kafkaTemplate);
            beanFactory.registerSingleton(getConsumerId() + "ReadListenerContainerFactory",
                    readListenerContainerFactory(readConsumerFactory, kafkaTemplate));
        };
    }

    private ProducerFactory<UUID, D> readReplyProducerFactory(String kafkaServer) {
        return new DefaultKafkaProducerFactory<>(getProducerConfigs(kafkaServer));
    }

    private KafkaTemplate<UUID, D> readKafkaTemplate(ProducerFactory<UUID, D> readReplyProducerFactory) {
        return new KafkaTemplate<>(readReplyProducerFactory);
    }
}
