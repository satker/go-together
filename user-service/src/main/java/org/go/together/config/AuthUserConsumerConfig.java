package org.go.together.config;

import org.go.together.dto.AuthUserDto;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.AUTH_USER;

@Component
public class AuthUserConsumerConfig extends ConsumerKafkaConfig<AuthUserDto> {
    @Override
    public String getConsumerId() {
        return AUTH_USER.getDescription();
    }
}