package org.go.together.controller;

import org.go.together.base.FindController;
import org.go.together.client.RouteInfoClient;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.TransportType;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
public class RouteInfoController extends FindController implements RouteInfoClient {
    @Override
    public Collection<SimpleDto> getTransportTypes() {
        return Arrays.stream(TransportType.values())
                .map(transportType -> new SimpleDto(transportType.name(), transportType.getDescription()))
                .collect(Collectors.toSet());
    }
}
