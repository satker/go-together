package org.go.together.mapper;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CommonMapper;
import org.go.together.base.Mapper;
import org.go.together.dto.*;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.model.Interest;
import org.go.together.model.Language;
import org.go.together.model.SystemUser;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper extends CommonMapper<UserDto, SystemUser> {
    private final CrudProducer<LocationDto> locationProducer;
    private final CrudProducer<GroupPhotoDto> groupPhotoProducer;
    private final Mapper<LanguageDto, Language> languageMapper;
    private final Mapper<InterestDto, Interest> interestMapper;

    @Override
    public UserDto toDto(SystemUser entity) {
        UserDto userDTO = new UserDto();
        userDTO.setDescription(entity.getDescription());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setId(entity.getId());
        userDTO.setLanguages(languageMapper.entitiesToDtos(entity.getLanguages()));
        userDTO.setLastName(entity.getLastName());
        userDTO.setLogin(entity.getLogin());
        userDTO.setMail(entity.getMail());
        userDTO.setRole(entity.getRole());
        userDTO.setInterests(interestMapper.entitiesToDtos(entity.getInterests()));
        asyncMapper.add("userLocationRead", () -> userDTO.setLocation(locationProducer.read(entity.getLocationId())));
        asyncMapper.add("userGroupPhotoRead", () -> userDTO.setGroupPhoto(groupPhotoProducer.read(entity.getGroupPhoto())));
        return userDTO;
    }

    @Override
    public SystemUser toEntity(UserDto dto) {
        SystemUser user = new SystemUser();
        user.setId(dto.getId());
        user.setMail(dto.getMail());
        user.setLogin(dto.getLogin());
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setDescription(dto.getDescription());
        user.setPassword(dto.getPassword());
        user.setInterests(dto.getInterests().stream()
                .map(interestMapper::dtoToEntity)
                .collect(Collectors.toSet()));
        user.setLanguages(dto.getLanguages().stream()
                .map(languageMapper::dtoToEntity)
                .collect(Collectors.toSet()));
        user.setRole(dto.getRole());
        return user;
    }

}
