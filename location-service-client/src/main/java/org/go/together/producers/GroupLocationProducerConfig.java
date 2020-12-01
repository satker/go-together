package org.go.together.producers;

import org.go.together.dto.GroupLocationDto;
import org.go.together.kafka.producer.config.ProducerKafkaConfig;
import org.springframework.stereotype.Component;

import static org.go.together.enums.LocationServiceInfo.GROUP_LOCATION;

@Component
public class GroupLocationProducerConfig extends ProducerKafkaConfig<GroupLocationDto> {
    @Override
    public String getConsumerId() {
        return GROUP_LOCATION.getDescription();
    }
}
