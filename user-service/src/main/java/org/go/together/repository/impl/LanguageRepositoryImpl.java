package org.go.together.repository.impl;

import org.go.together.model.Language;
import org.go.together.repository.CustomRepositoryImpl;
import org.go.together.repository.interfaces.LanguageRepository;
import org.springframework.stereotype.Repository;


@Repository
public class LanguageRepositoryImpl extends CustomRepositoryImpl<Language> implements LanguageRepository {
}
