package org.go.together.producers;

import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.GroupRouteInfoDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.RouteInfoServiceInfo.GROUP_ROUTE_INFO;

@Component
@EnableAutoConfigurationKafkaProducer(producerId = GROUP_ROUTE_INFO)
public class GroupRouteInfoProducer extends CrudClient<GroupRouteInfoDto> {
}
