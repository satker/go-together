package org.go.together.notification.correction;

import org.go.together.notification.dto.LanguageDto;
import org.go.together.validation.Validator;
import org.springframework.stereotype.Component;

@Component
public class LanguageValidator extends Validator<LanguageDto> {
    @Override
    public void getMapsForCheck(LanguageDto dto) {

    }
}
