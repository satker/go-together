package org.go.together.client;

import org.go.together.base.FindClient;
import org.go.together.dto.EventDto;
import org.go.together.dto.EventUserDto;
import org.go.together.dto.EventUserStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "event-service")
public interface EventClient extends FindClient {
    @GetMapping("/events/{id}")
    EventDto readEvent(@PathVariable("id") UUID id);

    @GetMapping("/events/{eventId}/users/statuses")
    EventUserStatus[] getUserStatuses();

    @DeleteMapping("/events/users")
    boolean deleteEventUserByEventId(@RequestBody EventUserDto eventUserDto);
}
