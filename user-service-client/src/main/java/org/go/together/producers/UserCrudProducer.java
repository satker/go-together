package org.go.together.producers;

import org.go.together.dto.UserDto;
import org.go.together.kafka.base.KafkaCrudClient;
import org.springframework.stereotype.Component;

@Component
public class UserCrudProducer extends KafkaCrudClient<UserDto> {
}

