package org.go.together.kafka.consumer.beanfactorypostprocessor;

import org.go.together.exceptions.ApplicationException;
import org.go.together.kafka.consumer.config.interfaces.CustomConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.SeekToCurrentErrorHandler;

import java.util.List;

@Configuration
public class ConsumerConfigBeanFactoryPostProcessor {
    @Bean
    public static BeanFactoryPostProcessor consumerBeanFactoryPostProcessor(@Value("${kafka.server}") String kafkaServer,
                                                                            @Value("${kafka.groupId}") String kafkaGroupId,
                                                                            List<CustomConsumerConfig> consumerConfigs) {
        return beanFactory -> consumerConfigs.forEach(consumerConfig -> consumerConfig.configureConsumer(kafkaServer, kafkaGroupId, beanFactory));
    }

    @Bean
    public SeekToCurrentErrorHandler errorHandler(ConsumerRecordRecoverer recoverer) {
        SeekToCurrentErrorHandler handler = new SeekToCurrentErrorHandler(recoverer);
        handler.addNotRetryableExceptions(ApplicationException.class);
        return handler;
    }
}
