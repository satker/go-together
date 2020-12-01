package org.go.together.producers;

import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.UserDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.USERS;

@Component
@EnableAutoConfigurationKafkaProducer
public class UserProducer extends CrudClient<UserDto> {
    @Override
    public String getConsumerId() {
        return USERS.getDescription();
    }
}

