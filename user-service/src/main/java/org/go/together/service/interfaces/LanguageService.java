package org.go.together.service.interfaces;

import org.go.together.base.CrudService;
import org.go.together.dto.LanguageDto;
import org.go.together.find.FindService;
import org.go.together.model.Language;

public interface LanguageService extends CrudService<LanguageDto>, FindService<Language> {
}
