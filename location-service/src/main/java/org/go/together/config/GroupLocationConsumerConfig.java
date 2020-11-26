package org.go.together.config;

import org.go.together.dto.GroupLocationDto;
import org.go.together.enums.ServiceInfo;
import org.go.together.kafka.config.ConsumerKafkaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupLocationConsumerConfig extends ConsumerKafkaConfig<GroupLocationDto> {
    @Override
    public String getConsumerId() {
        return ServiceInfo.GROUP_LOCATION.getDescription();
    }
}