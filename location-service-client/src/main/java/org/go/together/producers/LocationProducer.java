package org.go.together.producers;

import org.go.together.annotations.EnableAutoConfigurationKafkaProducer;
import org.go.together.dto.LocationDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.LocationServiceInfo.LOCATION;

@Component
@EnableAutoConfigurationKafkaProducer(producerId = LOCATION)
public class LocationProducer extends CrudClient<LocationDto> {
}
