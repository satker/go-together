package org.go.together.config;

import org.go.together.dto.GroupLocationDto;
import org.go.together.enums.LocationServiceInfo;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

@Component
public class GroupLocationConsumerConfig extends ConsumerKafkaConfig<GroupLocationDto> {
    @Override
    public String getConsumerId() {
        return LocationServiceInfo.GROUP_LOCATION.getDescription();
    }
}