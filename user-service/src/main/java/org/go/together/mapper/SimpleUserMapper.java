package org.go.together.mapper;

import org.go.together.client.ContentClient;
import org.go.together.dto.SimpleUserDto;
import org.go.together.exceptions.CannotFindEntityException;
import org.go.together.model.SystemUser;
import org.go.together.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class SimpleUserMapper implements Mapper<SimpleUserDto, SystemUser> {
    private final ContentClient contentClient;
    private final UserRepository userRepository;

    public SimpleUserMapper(ContentClient contentClient, UserRepository userRepository) {
        this.contentClient = contentClient;
        this.userRepository = userRepository;
    }

    @Override
    public SimpleUserDto entityToDto(SystemUser entity) {
        SimpleUserDto simpleUserDto = new SimpleUserDto();
        simpleUserDto.setId(entity.getId());
        simpleUserDto.setFirstName(entity.getFirstName());
        simpleUserDto.setLastName(entity.getLastName());
        simpleUserDto.setLogin(entity.getLogin());
        simpleUserDto.setUserPhoto(contentClient.readGroupPhotosById(entity.getGroupPhoto()).getPhotos()
                .iterator().next());
        return simpleUserDto;
    }

    @Override
    public SystemUser dtoToEntity(SimpleUserDto dto) {
        return userRepository.findById(dto.getId())
                .orElseThrow(() -> new CannotFindEntityException("Cannot find user " + dto.getLogin()));
    }
}
