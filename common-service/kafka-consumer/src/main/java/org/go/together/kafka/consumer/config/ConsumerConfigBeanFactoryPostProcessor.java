package org.go.together.kafka.consumer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConsumerConfigBeanFactoryPostProcessor {
    @Bean
    public static BeanFactoryPostProcessor consumerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                            @Value("${kafka.groupId}") String kafkaGroupId,
                                                                            List<CustomConsumerConfig> consumerConfigs) {
        return beanFactory -> consumerConfigs.forEach(consumerConfig -> consumerConfig.configureConsumer(kafkaServer, kafkaGroupId, beanFactory));
    }
}
