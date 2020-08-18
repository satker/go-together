package org.go.together.client;

import org.go.together.dto.IdDto;
import org.go.together.dto.MessageDto;
import org.go.together.find.client.FindClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

@FeignClient(name = "message-service")
public interface MessageClient extends FindClient {
    @GetMapping("events/{eventId}/messages/")
    Map<UUID, MessageDto> getAllChatsByEvent(@PathVariable("eventId") UUID eventId);

    @PutMapping("users/{userId}/messages/{otherUserId}")
    Set<MessageDto> sentMessageToAnotherUser(@PathVariable("userId") UUID myId,
                                             @PathVariable("otherUserId") UUID otherUser,
                                             @RequestBody MessageDto messageDto);

    @PutMapping("events/{eventId}/messages")
    IdDto sentMessageToEvent(@PathVariable("eventId") UUID eventId,
                             @RequestBody MessageDto messageDto);

    @PutMapping("users/{userId}/reviews")
    Set<MessageDto> sentReviewToUser(@PathVariable("userId") UUID userId,
                                     @RequestBody MessageDto messageDto);

    @PostMapping("users/{userId}/messages/{otherUserId}")
    Set<MessageDto> updateMessageToAnotherUser(@PathVariable("userId") UUID myId,
                                               @PathVariable("otherUserId") UUID otherUser,
                                               @RequestBody MessageDto messageDto);

    @PostMapping("events/{eventId}/messages")
    Set<MessageDto> updateMessageToEvent(@PathVariable("eventId") UUID eventId,
                                         @RequestBody MessageDto messageDto);

    @PostMapping("users/{userId}/reviews")
    Set<MessageDto> updateReviewToUser(@PathVariable("userId") UUID userId,
                                       @RequestBody MessageDto messageDto);

    @DeleteMapping("users/{userId}/messages/{otherUserId}")
    Set<MessageDto> deleteMessageToAnotherUser(@PathVariable("userId") UUID myId,
                                               @PathVariable("otherUserId") UUID otherUser,
                                               @RequestBody MessageDto messageDto);

    @DeleteMapping("events/{eventId}/messages")
    Set<MessageDto> deleteMessageToEvent(@PathVariable("eventId") UUID eventId,
                                         @RequestBody MessageDto messageDto);

    @DeleteMapping("users/{userId}/reviews")
    Set<MessageDto> deleteReviewToUser(@PathVariable("userId") UUID userId,
                                       @RequestBody MessageDto messageDto);
}
