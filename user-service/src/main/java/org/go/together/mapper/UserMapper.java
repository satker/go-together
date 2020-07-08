package org.go.together.mapper;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.dto.UserDto;
import org.go.together.logic.Mapper;
import org.go.together.model.SystemUser;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper implements Mapper<UserDto, SystemUser> {
    private final LocationClient locationClient;
    private final ContentClient contentClient;
    private final LanguageMapper languageMapper;
    private final InterestMapper interestMapper;

    public UserMapper(LocationClient locationClient, ContentClient contentClient,
                      LanguageMapper languageMapper, InterestMapper interestMapper) {
        this.locationClient = locationClient;
        this.contentClient = contentClient;
        this.languageMapper = languageMapper;
        this.interestMapper = interestMapper;
    }

    @Override
    public UserDto entityToDto(SystemUser entity) {
        UserDto userDTO = new UserDto();
        userDTO.setDescription(entity.getDescription());
        userDTO.setFirstName(entity.getFirstName());
        userDTO.setId(entity.getId());
        userDTO.setLanguages(entity.getLanguages().stream()
                .map(languageMapper::entityToDto)
                .collect(Collectors.toSet()));
        userDTO.setLastName(entity.getLastName());
        userDTO.setLocation(locationClient.getLocationById(entity.getLocationId()));
        userDTO.setLogin(entity.getLogin());
        userDTO.setMail(entity.getMail());
        userDTO.setRole(entity.getRole());
        userDTO.setInterests(entity.getInterests().stream()
                .map(interestMapper::entityToDto)
                .collect(Collectors.toSet()));
        userDTO.setGroupPhoto(contentClient.getGroupPhotosById(entity.getGroupPhoto()));
        return userDTO;
    }

    @Override
    public SystemUser dtoToEntity(UserDto dto) {
        SystemUser user = new SystemUser();
        user.setId(dto.getId());
        user.setMail(dto.getMail());
        user.setLogin(dto.getLogin());
        user.setLocationId(locationClient.saveLocation(dto.getLocation()).getId());
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
