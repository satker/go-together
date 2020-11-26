package org.go.together.producers;

import org.go.together.dto.GroupLocationDto;
import org.go.together.kafka.config.ProducerKafkaConfig;
import org.springframework.context.annotation.Configuration;

import static org.go.together.enums.ServiceInfo.GROUP_LOCATION;

@Configuration
public class GroupLocationProducerConfig extends ProducerKafkaConfig<GroupLocationDto> {
    @Override
    public String getConsumerId() {
        return GROUP_LOCATION.getDescription();
    }
}
