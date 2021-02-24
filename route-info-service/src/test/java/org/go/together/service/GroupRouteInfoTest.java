package org.go.together.service;

import org.go.together.context.RepositoryContext;
import org.go.together.dto.*;
import org.go.together.enums.CrudOperation;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.kafka.producers.ValidationProducer;
import org.go.together.model.GroupRouteInfo;
import org.go.together.tests.CrudServiceCommonTest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = RepositoryContext.class)
public class GroupRouteInfoTest extends CrudServiceCommonTest<GroupRouteInfo, GroupRouteInfoDto> {
    @Autowired
    private CrudProducer<LocationDto> locationProducer;

    @Autowired
    private ValidationProducer<LocationDto> locationValidate;

    @Override
    @BeforeEach
    public void init() {
        super.init();
        dto.getInfoRoutes().forEach(this::prepareDto);
        updatedDto.getInfoRoutes().forEach(this::prepareDto);
    }

    @Override
    protected GroupRouteInfoDto createDto() {
        GroupRouteInfoDto groupRouteInfoDto = factory.manufacturePojo(GroupRouteInfoDto.class);
        Collection<RouteInfoDto> infoRoutes = groupRouteInfoDto.getInfoRoutes();
        Set<Integer> numbers = IntStream.rangeClosed(1, infoRoutes.size())
                .boxed()
                .collect(Collectors.toSet());
        Iterator<RouteInfoDto> iterator = infoRoutes.iterator();
        for (Integer number : numbers) {
            RouteInfoDto routeInfoDto = iterator.next();
            routeInfoDto.setRouteNumber(number);
            enrichWithCorrectEndStartRoute(infoRoutes.size(), number, routeInfoDto);
        }
        return groupRouteInfoDto;
    }

    private void enrichWithCorrectEndStartRoute(int locationsSize, Integer number, RouteInfoDto routeInfoDto) {
        if (number == 1) {
            routeInfoDto.setIsStart(true);
            routeInfoDto.setIsEnd(false);
        } else if (number == locationsSize) {
            routeInfoDto.setIsStart(false);
            routeInfoDto.setIsEnd(true);
        } else {
            routeInfoDto.setIsStart(false);
            routeInfoDto.setIsEnd(false);
        }
    }

    @Override
    protected void checkDtos(GroupRouteInfoDto dto, GroupRouteInfoDto savedObject, CrudOperation operation) {
        if (operation == CrudOperation.CREATE) {
            assertEquals(dto.getGroupId(), savedObject.getGroupId());
            assertEquals(dto.getInfoRoutes().size(), dto.getInfoRoutes().size());
        } else if (operation == CrudOperation.UPDATE) {
            assertEquals(dto.getInfoRoutes().size(), dto.getInfoRoutes().size());
        }
    }

    private void prepareDto(RouteInfoDto routeInfoDto) {
        when(locationValidate.validate(any(UUID.class), eq(routeInfoDto.getLocation()))).thenReturn(new ValidationMessageDto(EMPTY));
        when(locationProducer.create(any(UUID.class), eq(routeInfoDto.getLocation()))).thenReturn(new IdDto(routeInfoDto.getLocation().getId()));
        when(locationProducer.update(any(UUID.class), eq(routeInfoDto.getLocation()))).thenReturn(new IdDto(routeInfoDto.getLocation().getId()));
        when(locationProducer.read(any(UUID.class), eq(routeInfoDto.getLocation().getId()))).thenReturn(routeInfoDto.getLocation());
    }
}
