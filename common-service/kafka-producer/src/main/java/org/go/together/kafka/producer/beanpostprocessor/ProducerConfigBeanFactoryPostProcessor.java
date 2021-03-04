package org.go.together.kafka.producer.beanpostprocessor;

import brave.Tracer;
import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.Dto;
import org.go.together.kafka.producer.base.CrudClient;
import org.go.together.kafka.producer.config.interfaces.KafkaProducerConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Stream;

@Configuration
public class ProducerConfigBeanFactoryPostProcessor {
    @Bean
    public static BeanFactoryPostProcessor producerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                            @Value("${kafka.groupId}") String kafkaGroupId,
                                                                            @Autowired List<KafkaProducerConfigurator> configurators,
                                                                            @Autowired Tracer tracer) {
        return beanFactory -> Stream.of(beanFactory.getBeanNamesForType(CrudClient.class))
                .map(beanFactory::getBean)
                .filter(client -> client.getClass().isAnnotationPresent(EnableAutoConfigurationKafkaProducer.class))
                .map(client -> (CrudClient<? extends Dto>) client)
                .map(ProducerConfigBeanFactoryPostProcessor::getProducerRights)
                .forEach(producerRights -> configurators.forEach(configurator ->
                        configurator.configure(kafkaServer, kafkaGroupId, beanFactory, producerRights, tracer)));
    }

    private static <D extends Dto> ProducerRights<D> getProducerRights(CrudClient<D> producer) {
        EnableAutoConfigurationKafkaProducer configurationKafkaProducer =
                producer.getClass().getAnnotation(EnableAutoConfigurationKafkaProducer.class);
        return ProducerRights.<D>builder()
                .producer(producer)
                .isCreate(configurationKafkaProducer.isCreate())
                .isFind(configurationKafkaProducer.isFind())
                .isRead(configurationKafkaProducer.isRead())
                .isUpdate(configurationKafkaProducer.isUpdate())
                .isValidate(configurationKafkaProducer.isValidate())
                .isDelete(configurationKafkaProducer.isDelete())
                .producerId(configurationKafkaProducer.producerId()).build();
    }
}
