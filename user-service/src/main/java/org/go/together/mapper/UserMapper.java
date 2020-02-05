package org.go.together.mapper;

import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.dto.UserDto;
import org.go.together.interfaces.Mapper;
import org.go.together.model.SystemUser;
import org.go.together.repository.UserRepository;
import org.go.together.service.LanguageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements Mapper<UserDto, SystemUser> {
    private final LocationClient locationClient;
    private LanguageService languageService;
    private final ContentClient contentClient;
    private final UserRepository userRepository;
    private final LanguageMapper languageMapper;
    private final InterestMapper interestMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserMapper(LocationClient locationClient, ContentClient contentClient, UserRepository userRepository,
                      LanguageMapper languageMapper, InterestMapper interestMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.locationClient = locationClient;
        this.contentClient = contentClient;
        this.userRepository = userRepository;
        this.languageMapper = languageMapper;
        this.interestMapper = interestMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setLanguageService(LanguageService languageService) {
        this.languageService = languageService;
    }

    public UserDto entityToDto(SystemUser user) {
        UserDto userDTO = new UserDto();
        userDTO.setDescription(user.getDescription());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setId(user.getId());
        /*userDTO.setLanguages(userRepository.getLanguagesByUser(user.getId()).stream()
                .map(languageMapper::entityToDto)
                .collect(Collectors.toSet()));*/
        userDTO.setLastName(user.getLastName());
        //userDTO.setLocation(locationClient.findCityById(user.getLocationId()));
        userDTO.setLogin(user.getLogin());
        userDTO.setMail(user.getMail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRole(user.getRole());
        /*userDTO.setInterests(userRepository.getInterestsByUser(user.getId()).stream()
                .map(interestMapper::entityToDto)
                .collect(Collectors.toSet()));*/
        //userDTO.setUserPhoto(contentClient.findPhotoById(user.getPhotoId()));
        return userDTO;
    }

    @Override
    public SystemUser dtoToEntity(UserDto dto) {
        SystemUser user = new SystemUser();
        if (dto.getId() != null) {
            user.setId(dto.getId());
        }
        return null;
    }

}
