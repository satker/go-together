package org.go.together.config;

import org.go.together.dto.LocationDto;
import org.go.together.enums.LocationServiceInfo;
import org.go.together.kafka.consumer.config.ConsumerKafkaConfig;
import org.springframework.stereotype.Component;

@Component
public class LocationConsumerConfig extends ConsumerKafkaConfig<LocationDto> {
    @Override
    public String getConsumerId() {
        return LocationServiceInfo.LOCATION;
    }
}
