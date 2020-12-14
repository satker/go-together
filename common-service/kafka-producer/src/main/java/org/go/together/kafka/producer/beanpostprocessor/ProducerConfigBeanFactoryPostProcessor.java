package org.go.together.kafka.producer.beanpostprocessor;

import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.Dto;
import org.go.together.kafka.producer.base.CrudClient;
import org.go.together.kafka.producer.config.ProducerKafkaConfig;
import org.go.together.kafka.producer.enums.ProducerPostfix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.stream.Stream;

import static org.go.together.kafka.producer.enums.ProducerPostfix.*;

@Configuration
public class ProducerConfigBeanFactoryPostProcessor {
    @Bean
    public static BeanFactoryPostProcessor producerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                            @Value("${kafka.groupId}") String kafkaGroupId) {
        return beanFactory -> Stream.of(beanFactory.getBeanNamesForType(CrudClient.class))
                .map(beanFactory::getBean)
                .filter(client -> client.getClass().isAnnotationPresent(EnableAutoConfigurationKafkaProducer.class))
                .map(ProducerConfigBeanFactoryPostProcessor::getProducerRights)
                .forEach(producerRights -> configure(producerRights, kafkaServer, kafkaGroupId, beanFactory));
    }

    private static ProducerRights getProducerRights(Object producer) {
        CrudClient<?> crudClient = (CrudClient<?>) producer;
        EnableAutoConfigurationKafkaProducer configurationKafkaProducer =
                crudClient.getClass().getAnnotation(EnableAutoConfigurationKafkaProducer.class);
        return ProducerRights.builder()
                .producer(crudClient)
                .isCreate(configurationKafkaProducer.isCreate())
                .isFind(configurationKafkaProducer.isFind())
                .isRead(configurationKafkaProducer.isRead())
                .isUpdate(configurationKafkaProducer.isUpdate())
                .isValidate(configurationKafkaProducer.isValidate())
                .isDelete(configurationKafkaProducer.isDelete())
                .producerId(configurationKafkaProducer.producerId()).build();
    }

    private static void configure(ProducerRights producerRights, String kafkaServer,
                                  String kafkaGroupId, ConfigurableListableBeanFactory configurableListableBeanFactory) {
        ProducerKafkaConfig<Dto> producerKafkaConfig = ProducerKafkaConfig.create(producerRights.getProducerId());
        producerKafkaConfig.configureProducer(kafkaServer, kafkaGroupId, configurableListableBeanFactory, producerRights);
        enrichCrudClient(producerRights, configurableListableBeanFactory);
    }

    private static void enrichCrudClient(ProducerRights producerRights, ConfigurableListableBeanFactory beanFactory) {
        String consumerId = producerRights.getProducerId();
        CrudClient<? extends Dto> crudClient = producerRights.getProducer();
        if (producerRights.isCreate()) {
            crudClient.setCreateKafkaProducer(getKafkaProducer(beanFactory, consumerId, CREATE));
        }
        if (producerRights.isUpdate()) {
            crudClient.setUpdateKafkaProducer(getKafkaProducer(beanFactory, consumerId, UPDATE));
        }
        if (producerRights.isRead()) {
            crudClient.setReadKafkaProducer(getKafkaProducer(beanFactory, consumerId, READ));
        }
        if (producerRights.isDelete()) {
            crudClient.setDeleteKafkaProducer(getKafkaProducer(beanFactory, consumerId, DELETE));
        }
        if (producerRights.isFind()) {
            crudClient.setFindKafkaProducer(getKafkaProducer(beanFactory, consumerId, FIND));
        }
        if (producerRights.isValidate()) {
            crudClient.setValidateKafkaProducers(getKafkaProducer(beanFactory, consumerId, VALIDATE));
        }
    }

    private static <T> T getKafkaProducer(ConfigurableListableBeanFactory beanFactory,
                                          String consumerId,
                                          ProducerPostfix producerPostfix) {
        return (T) beanFactory.getBean(consumerId + producerPostfix.getDescription());
    }
}
