package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.go.together.dto.LocationDto;
import org.go.together.dto.RouteInfoDto;
import org.go.together.kafka.producers.ValidationProducer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class RouteInfoValidator extends CommonValidator<RouteInfoDto> {
    private final ValidationProducer<LocationDto> locationValidator;

    @Override
    public Map<String, Function<RouteInfoDto, ?>> getMapsForCheck(UUID requestId) {
        return Map.of(
                "location", routeInfoDto -> locationValidator.validate(requestId, routeInfoDto.getLocation()),
                "movement date", RouteInfoDto::getMovementDate,
                "cost", RouteInfoDto::getCost,
                "movement duration", RouteInfoDto::getMovementDuration,
                "transport", RouteInfoDto::getTransportType
        );
    }
}
