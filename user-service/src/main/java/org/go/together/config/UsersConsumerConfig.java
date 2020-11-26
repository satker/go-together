package org.go.together.config;

import org.go.together.dto.UserDto;
import org.go.together.enums.ServiceInfo;
import org.go.together.kafka.config.ConsumerKafkaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UsersConsumerConfig extends ConsumerKafkaConfig<UserDto> {
    @Override
    public String getConsumerId() {
        return ServiceInfo.USERS.getDescription();
    }
}