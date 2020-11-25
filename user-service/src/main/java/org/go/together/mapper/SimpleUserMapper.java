package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.Mapper;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.SimpleUserDto;
import org.go.together.kafka.interfaces.producers.CommonCrudProducer;
import org.go.together.model.SystemUser;
import org.go.together.repository.interfaces.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleUserMapper implements Mapper<SimpleUserDto, SystemUser> {
    private final CommonCrudProducer<GroupPhotoDto> groupPhotoProducer;
    private final UserRepository userRepository;

    @Override
    public SimpleUserDto entityToDto(SystemUser entity) {
        SimpleUserDto simpleUserDto = new SimpleUserDto();
        simpleUserDto.setId(entity.getId());
        simpleUserDto.setFirstName(entity.getFirstName());
        simpleUserDto.setLastName(entity.getLastName());
        simpleUserDto.setLogin(entity.getLogin());
        simpleUserDto.setUserPhoto(groupPhotoProducer.read(entity.getId(), entity.getGroupPhoto()).getPhotos()
                .iterator().next());
        return simpleUserDto;
    }

    @Override
    public SystemUser dtoToEntity(SimpleUserDto dto) {
        return userRepository.findByIdOrThrow(dto.getId());
    }
}
