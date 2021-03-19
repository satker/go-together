package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.dto.GroupPhotoDto;
import org.go.together.dto.SimpleUserDto;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.SystemUser;
import org.go.together.repository.interfaces.UserRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SimpleUserMapper extends CommonMapper<SimpleUserDto, SystemUser> {
    private final CrudProducer<GroupPhotoDto> groupPhotoProducer;
    private final UserRepository userRepository;

    @Override
    public SimpleUserDto toDto(SystemUser entity) {
        SimpleUserDto simpleUserDto = new SimpleUserDto();
        simpleUserDto.setId(entity.getId());
        simpleUserDto.setFirstName(entity.getFirstName());
        simpleUserDto.setLastName(entity.getLastName());
        simpleUserDto.setLogin(entity.getLogin());
        simpleUserDto.setUserPhoto(groupPhotoProducer.read(entity.getGroupPhoto()).getPhotos()
                .iterator().next());
        return simpleUserDto;
    }

    @Override
    public SystemUser toEntity(SimpleUserDto dto) {
        return userRepository.findByIdOrThrow(dto.getId());
    }
}
