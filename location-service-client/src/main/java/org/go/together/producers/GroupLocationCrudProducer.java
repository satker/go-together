package org.go.together.producers;

import org.go.together.dto.GroupLocationDto;
import org.go.together.kafka.producer.base.CrudClient;
import org.springframework.stereotype.Component;

import static org.go.together.enums.LocationServiceInfo.GROUP_LOCATION;

@Component
public class GroupLocationCrudProducer extends CrudClient<GroupLocationDto> {
    @Override
    public String getConsumerId() {
        return GROUP_LOCATION.getDescription();
    }
}
