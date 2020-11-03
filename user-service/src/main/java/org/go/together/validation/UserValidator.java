package org.go.together.validation;

import lombok.RequiredArgsConstructor;
import org.go.together.client.ContentClient;
import org.go.together.client.LocationClient;
import org.go.together.dto.UserDto;
import org.go.together.enums.CrudOperation;
import org.go.together.repository.interfaces.InterestRepository;
import org.go.together.repository.interfaces.LanguageRepository;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserValidator extends CommonValidator<UserDto> {
    private final LocationClient locationClient;
    private final LanguageRepository languageRepository;
    private final InterestRepository interestRepository;
    private final ContentClient contentClient;

    @Override
    public String commonValidation(UserDto dto, CrudOperation crudOperation) {
        StringBuilder errors = new StringBuilder();

        if (crudOperation == CrudOperation.CREATE) {
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
        }

        return errors.toString();
    }

    @Override
    public void getMapsForCheck(UserDto dto) {
        super.STRINGS_FOR_BLANK_CHECK = Map.of(
                "first name", UserDto::getFirstName,
                "last name", UserDto::getLastName,
                "description", UserDto::getDescription,
                "login", UserDto::getLogin,
                "mail", UserDto::getMail);
        super.ANOTHER_SERVICE_DTO_CORRECT_CHECK = Map.of(
                locationClient, dto.getLocation(),
                contentClient, dto.getGroupPhoto()
        );
    }
}
