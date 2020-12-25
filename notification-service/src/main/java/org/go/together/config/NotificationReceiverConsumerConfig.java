package org.go.together.config;

import org.go.together.dto.NotificationReceiverDto;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.NotificationServiceInfo.NOTIFICATION_RECEIVER;

@Component
public class NotificationReceiverConsumerConfig extends ConsumerKafkaConfig<NotificationReceiverDto> {
    @Override
    public String getConsumerId() {
        return NOTIFICATION_RECEIVER;
    }
}
