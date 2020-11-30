package org.go.together.producers;

import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.kafka.config.producers.ProducerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.RouteInfoServiceInfo.GROUP_ROUTE_INFO;

@Component
public class GroupRouteInfoProducerConfig extends ProducerKafkaConfig<GroupRouteInfoDto> {
    @Override
    public String getConsumerId() {
        return GROUP_ROUTE_INFO.getDescription();
    }
}
