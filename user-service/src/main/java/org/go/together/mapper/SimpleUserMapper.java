package org.go.together.mapper;

import org.go.together.client.ContentClient;
import org.go.together.dto.SimpleUserDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.SystemUser;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SimpleUserMapper implements Mapper<SimpleUserDto, SystemUser> {
    private final ContentClient contentClient;

    public SimpleUserMapper(ContentClient contentClient) {
        this.contentClient = contentClient;
    }

    @Override
    public SimpleUserDto entityToDto(SystemUser entity) {
        SimpleUserDto simpleUserDto = new SimpleUserDto();
        simpleUserDto.setId(entity.getId());
        simpleUserDto.setFirstName(entity.getFirstName());
        simpleUserDto.setLastName(entity.getLastName());
        simpleUserDto.setLogin(entity.getLogin());
        simpleUserDto.setUserPhoto(contentClient.getPhotosByIds(Collections.singleton(entity.getPhotoIds().iterator().next()))
                .iterator().next());
        return simpleUserDto;
    }

    @Override
    public SystemUser dtoToEntity(SimpleUserDto dto) {
        return null;
    }
}
