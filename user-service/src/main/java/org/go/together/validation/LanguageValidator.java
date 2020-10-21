package org.go.together.validation;

import org.go.together.dto.LanguageDto;
import org.go.together.validation.impl.CommonValidator;
import org.springframework.stereotype.Component;

@Component
public class LanguageValidator extends CommonValidator<LanguageDto> {
    @Override
    public void getMapsForCheck(LanguageDto dto) {

    }
}
