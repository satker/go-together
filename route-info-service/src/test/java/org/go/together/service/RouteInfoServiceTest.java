package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.IdDto;
import org.go.together.dto.LocationDto;
import org.go.together.dto.RouteInfoDto;
import org.go.together.dto.ValidationMessageDto;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.kafka.producers.ValidationProducer;
import org.go.together.model.RouteInfo;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.UUID;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
public class RouteInfoServiceTest extends CrudServiceCommonTest<RouteInfo, RouteInfoDto> {
    @Autowired
    private CrudProducer<LocationDto> locationProducer;

    @Autowired
    private ValidationProducer<LocationDto> locationValidate;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        prepareDto(dto);
        prepareDto(updatedDto);
    }

    @Override
    protected RouteInfoDto createDto() {
        return factory.manufacturePojo(RouteInfoDto.class);
    }

    private void prepareDto(RouteInfoDto routeInfoDto) {
        when(locationValidate.validate(any(UUID.class), eq(routeInfoDto.getLocation()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(locationProducer.create(any(UUID.class), eq(routeInfoDto.getLocation()))).thenReturn(new IdDto(routeInfoDto.getLocation().getId()));
        when(locationProducer.update(any(UUID.class), eq(routeInfoDto.getLocation()))).thenReturn(new IdDto(routeInfoDto.getLocation().getId()));
        when(locationProducer.read(any(UUID.class), eq(routeInfoDto.getLocation().getId()))).thenReturn(routeInfoDto.getLocation());
    }
}