package org.go.together.config;

import org.go.together.dto.UserDto;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.USERS;

@Component
public class UsersConsumerConfig extends ConsumerKafkaConfig<UserDto> {
    @Override
    public String getConsumerId() {
        return USERS.getDescription();
    }
}