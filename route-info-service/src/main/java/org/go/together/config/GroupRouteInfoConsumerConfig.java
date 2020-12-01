package org.go.together.config;

import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.RouteInfoServiceInfo.GROUP_ROUTE_INFO;

@Component
public class GroupRouteInfoConsumerConfig extends ConsumerKafkaConfig<GroupRouteInfoDto> {
    @Override
    public String getConsumerId() {
        return GROUP_ROUTE_INFO.getDescription();
    }
}