package org.go.together.producers;

import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.kafka.config.ProducerKafkaConfig;
import org.springframework.context.annotation.Configuration;

import static org.go.together.enums.ServiceInfo.GROUP_ROUTE_INFO;

@Configuration
public class GroupRouteInfoProducerConfig extends ProducerKafkaConfig<GroupRouteInfoDto> {
    @Override
    public String getConsumerId() {
        return GROUP_ROUTE_INFO.getDescription();
    }
}
