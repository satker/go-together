package org.go.together.validation;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.dto.LocationDto;
import org.go.together.dto.UserDto;
import org.go.together.logic.Validator;
import org.go.together.repository.LanguageRepository;
import org.springframework.stereotype.Service;

@Service
public class UserValidator extends Validator<UserDto> {
    private LocationClient locationClient;
    private LanguageRepository languageRepository;
    private ContentClient contentClient;

    public UserValidator(LocationClient locationClient,
                         LanguageRepository languageRepository,
                         ContentClient contentClient) {
        this.locationClient = locationClient;
        this.contentClient = contentClient;
        this.languageRepository = languageRepository;
    }

    @Override
    public String validateForCreateCustom(UserDto dto) {
        StringBuilder errors = new StringBuilder();
        LocationDto locationById = locationClient.getLocationById(dto.getLocation().getId());
        if (locationById == null) {
            errors.append("Cannot find location");
        }

        if (dto.getLanguages().isEmpty() || dto.getLanguages()
                .stream()
                .anyMatch(lang -> !languageRepository.findById(lang.getId()).isPresent())) {
            errors.append("User languages are empty or incorrect");
        }

        String validatePhoto = contentClient.validate(dto.getUserPhoto());
        if (StringUtils.isNotBlank(validatePhoto)) {
            errors.append(validatePhoto);
        }

        return errors.toString();
    }

    @Override
    public void getMapsForCheck(UserDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = ImmutableMap.<String, String>builder()
                .put("first name", dto.getFirstName())
                .put("last name", dto.getLastName())
                .put("description", dto.getDescription())
                .put("login", dto.getLogin())
                .put("mail", dto.getMail())
                .build();
    }
}
