package org.go.together.notification.correction;

import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.StringUtils;
import org.go.together.enums.CrudOperation;
import org.go.together.notification.client.ContentClient;
import org.go.together.notification.client.LocationClient;
import org.go.together.notification.dto.UserDto;
import org.go.together.notification.repository.InterestRepository;
import org.go.together.notification.repository.LanguageRepository;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class UserValidator extends Validator<UserDto> {
    private final LocationClient locationClient;
    private final LanguageRepository languageRepository;
    private final InterestRepository interestRepository;
    private final ContentClient contentClient;

    public UserValidator(LocationClient locationClient,
                         LanguageRepository languageRepository,
                         ContentClient contentClient,
                         InterestRepository interestRepository) {
        this.locationClient = locationClient;
        this.contentClient = contentClient;
        this.languageRepository = languageRepository;
        this.interestRepository = interestRepository;
    }

    @Override
    public String commonValidation(UserDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        if (crudOperation == CrudOperation.CREATE) {
            String validatedLocation = locationClient.validateRoute(dto.getLocation());
            if (StringUtils.isNotBlank(validatedLocation)) {
                errors.append(validatedLocation);
            }

            if (dto.getLanguages().isEmpty() || dto.getLanguages()
                    .stream()
                    .anyMatch(lang -> languageRepository.findById(lang.getId()).isEmpty())) {
                errors.append("User languages are empty or incorrect");
            }

            if (dto.getInterests().isEmpty() || dto.getInterests()
                    .stream()
                    .anyMatch(lang -> interestRepository.findById(lang.getId()).isEmpty())) {
                errors.append("User interests are empty or incorrect");
            }

            String contentValidation = contentClient.validate(dto.getGroupPhoto());
            if (StringUtils.isNotBlank(contentValidation)) {
                errors.append(contentValidation);
            }
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
