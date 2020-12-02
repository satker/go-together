package org.go.together.producer;

import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.AuthUserDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.UserServiceInfo.AUTH_USER;

@Component
@EnableAutoConfigurationKafkaProducer(producerId = AUTH_USER)
public class AuthUserProducer extends CrudClient<AuthUserDto> {
}