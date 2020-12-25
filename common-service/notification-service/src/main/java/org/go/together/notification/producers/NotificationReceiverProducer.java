package org.go.together.notification.producers;

import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.NotificationReceiverDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.NotificationServiceInfo.NOTIFICATION_RECEIVER;

@Component
@EnableAutoConfigurationKafkaProducer(producerId = NOTIFICATION_RECEIVER)
public class NotificationReceiverProducer extends CrudClient<NotificationReceiverDto> {
}
