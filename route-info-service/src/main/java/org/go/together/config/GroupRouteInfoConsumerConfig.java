package org.go.together.config;

import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.enums.ServiceInfo;
import org.go.together.kafka.config.ConsumerKafkaConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GroupRouteInfoConsumerConfig extends ConsumerKafkaConfig<GroupRouteInfoDto> {
    @Override
    public String getConsumerId() {
        return ServiceInfo.GROUP_ROUTE_INFO.getDescription();
    }
}