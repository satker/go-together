package org.go.together.producers;

import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.NotificationMessageDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.NotificationServiceInfo.NOTIFICATION_MESSAGE;

@Component
@EnableAutoConfigurationKafkaProducer(producerId = NOTIFICATION_MESSAGE)
public class NotificationMessageProducer extends CrudClient<NotificationMessageDto> {
}
