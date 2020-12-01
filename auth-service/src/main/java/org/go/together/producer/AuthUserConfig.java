package org.go.together.producer;

import org.go.together.dto.AuthUserDto;
import org.go.together.kafka.producer.config.ProducerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.AUTH_USER;

@Component
public class AuthUserConfig extends ProducerKafkaConfig<AuthUserDto> {
    @Override
    public String getConsumerId() {
        return AUTH_USER.getDescription();
    }
}
