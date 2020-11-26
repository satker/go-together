package org.go.together.producers;

import org.go.together.dto.UserDto;
import org.go.together.kafka.config.ProducerKafkaConfig;
import org.springframework.context.annotation.Configuration;

import static org.go.together.enums.ServiceInfo.USERS;

@Configuration
public class UserProducerConfig extends ProducerKafkaConfig<UserDto> {
    @Override
    public String getConsumerId() {
        return USERS.getDescription();
    }
}

