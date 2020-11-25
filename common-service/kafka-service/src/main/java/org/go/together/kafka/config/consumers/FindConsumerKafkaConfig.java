package org.go.together.kafka.config.consumers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class FindConsumerKafkaConfig {
    @Value("${kafka.server}")
    private String kafkaServer;

    @Value("${kafka.groupId}")
    private String kafkaGroupId;

}
