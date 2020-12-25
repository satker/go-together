package org.go.together.config;

import org.go.together.dto.NotificationMessageDto;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.NotificationServiceInfo.NOTIFICATION_MESSAGE;

@Component
public class NotificationMessageConsumersConfig extends ConsumerKafkaConfig<NotificationMessageDto> {
    @Override
    public String getConsumerId() {
        return NOTIFICATION_MESSAGE;
    }
}
