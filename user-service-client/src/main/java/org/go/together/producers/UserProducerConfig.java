package org.go.together.producers;

import org.go.together.dto.UserDto;
import org.go.together.kafka.config.producers.ProducerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.USERS;

@Component
public class UserProducerConfig extends ProducerKafkaConfig<UserDto> {
    @Override
    public String getConsumerId() {
        return USERS.getDescription();
    }
}
