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

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.kafka.producer.enums.ProducerPostfix.*;

@Configuration
public class ProducerConfigBeanFactoryPostProcessor {
    @Bean
    public static BeanFactoryPostProcessor producerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                            @Value("${kafka.groupId}") String kafkaGroupId) {
        return beanFactory -> {
            Set<CrudClient<?>> crudClients = Stream.of(beanFactory.getBeanNamesForType(CrudClient.class))
                    .map(beanFactory::getBean)
                    .filter(client -> client.getClass().isAnnotationPresent(EnableAutoConfigurationKafkaProducer.class))
                    .map(producer -> (CrudClient<?>) producer)
                    .collect(Collectors.toSet());

            crudClients.stream()
                    .map(CrudClient::getConsumerId)
                    .map(ProducerKafkaConfig::create)
                    .forEach(producerKafkaConfig -> producerKafkaConfig.configureProducer(kafkaServer, kafkaGroupId, beanFactory));

            crudClients.forEach(producer -> enrichCrudClient(producer, beanFactory));
        };
    }

    private static <D extends Dto> void enrichCrudClient(CrudClient<D> crudClient, ConfigurableListableBeanFactory beanFactory) {
        String consumerId = crudClient.getConsumerId();
        crudClient.setCreateKafkaProducer(getKafkaProducer(beanFactory, consumerId, CREATE));
        crudClient.setUpdateKafkaProducer(getKafkaProducer(beanFactory, consumerId, UPDATE));
        crudClient.setReadKafkaProducer(getKafkaProducer(beanFactory, consumerId, READ));
        crudClient.setDeleteKafkaProducer(getKafkaProducer(beanFactory, consumerId, DELETE));
        crudClient.setFindKafkaProducer(getKafkaProducer(beanFactory, consumerId, FIND));
        crudClient.setValidateKafkaProducers(getKafkaProducer(beanFactory, consumerId, VALIDATE));
    }

    private static <T> T getKafkaProducer(ConfigurableListableBeanFactory beanFactory,
                                          String consumerId,
                                          ProducerPostfix producerPostfix) {
        return (T) beanFactory.getBean(consumerId + producerPostfix.getDescription());
    }
}
