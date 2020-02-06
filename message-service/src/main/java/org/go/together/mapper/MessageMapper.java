package org.go.together.mapper;

import org.go.together.client.UserClient;
import org.go.together.dto.MessageDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.Message;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper implements Mapper<MessageDto, Message> {
    private final UserClient userClient;

    public MessageMapper(UserClient userClient) {
        this.userClient = userClient;
    }

    @Override
    public MessageDto entityToDto(Message entity) {
        MessageDto messageDto = new MessageDto();
        messageDto.setId(entity.getId());
        messageDto.setDate(entity.getDate());
        messageDto.setMessageType(entity.getMessageType());
        messageDto.setRating(entity.getRating());
        messageDto.setRecipientId(entity.getRecipientId());
        messageDto.setAuthor(userClient.findById(entity.getAuthorId()));
        messageDto.setMessage(entity.getMessage());
        return messageDto;
    }

    @Override
    public Message dtoToEntity(MessageDto dto) {
        Message message = new Message();
        message.setId(dto.getId());
        message.setAuthorId(dto.getAuthor().getId());
        message.setDate(dto.getDate());
        message.setMessageType(dto.getMessageType());
        message.setMessage(dto.getMessage());
        message.setRating(dto.getRating());
        message.setRecipientId(dto.getRecipientId());
        return message;
    }
}
