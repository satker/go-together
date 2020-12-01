package org.go.together.kafka.producer.beanpostprocessor;

import org.go.together.kafka.producer.config.CustomProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ProducerConfigBeanFactoryPostProcessor {
    @Bean
    public static BeanFactoryPostProcessor producerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                            @Value("${kafka.groupId}") String kafkaGroupId,
                                                                            List<CustomProducerConfig> producerConfigList) {
        return beanFactory -> producerConfigList.forEach(producerConfig -> producerConfig.configureProducer(kafkaServer, kafkaGroupId, beanFactory));
    }
}
