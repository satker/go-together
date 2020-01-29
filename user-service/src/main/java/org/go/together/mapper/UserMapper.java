package org.go.together.mapper;

/*
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.dto.Role;
import org.go.together.dto.UserDto;
import org.go.together.interfaces.Mapper;
import org.go.together.repository.UserRepository;
import org.go.together.service.LanguageService;
import org.jooq.generated.tables.records.AppUserRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserMapper implements Mapper<UserDto, AppUserRecord> {
    private final LocationClient locationClient;
    private LanguageService languageService;
    private final ContentClient contentClient;
    private final UserRepository userRepository;
    private final LanguageMapper languageMapper;
    private final InterestMapper interestMapper;

    public UserMapper(LocationClient locationClient, ContentClient contentClient, UserRepository userRepository,
                      LanguageMapper languageMapper, InterestMapper interestMapper) {
        this.locationClient = locationClient;
        this.contentClient = contentClient;
        this.userRepository = userRepository;
        this.languageMapper = languageMapper;
        this.interestMapper = interestMapper;
    }

    @Autowired
    public void setLanguageService(LanguageService languageService) {
        this.languageService = languageService;
    }

    public UserDto entityToDto(AppUserRecord user) {
        UserDto userDTO = new UserDto();
        userDTO.setDescription(user.getDescription());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setId(user.getId());
        userDTO.setLanguages(userRepository.getLanguagesByUser(user.getId()).stream()
                .map(languageMapper::entityToDto)
                .collect(Collectors.toSet()));
        userDTO.setLastName(user.getLastName());
        //userDTO.setLocation(locationClient.findCityById(user.getLocationId()));
        userDTO.setLogin(user.getLogin());
        userDTO.setMail(user.getMail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(Role.valueOf(user.getRole()));
        userDTO.setInterests(userRepository.getInterestsByUser(user.getId()).stream()
                .map(interestMapper::entityToDto)
                .collect(Collectors.toSet()));
        //userDTO.setUserPhoto(contentClient.findPhotoById(user.getPhotoId()));
        return userDTO;
    }

    @Override
    public AppUserRecord dtoToEntity(UserDto dto) {
        return null;
    }

}
*/
