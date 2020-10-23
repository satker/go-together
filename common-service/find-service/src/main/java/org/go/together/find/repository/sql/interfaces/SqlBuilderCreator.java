package org.go.together.find.repository.sql.interfaces;

import org.go.together.repository.CustomRepository;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.SqlBuilder;

public interface SqlBuilderCreator<E extends IdentifiedEntity> {
    SqlBuilder<E> getSqlBuilder(String mainField, CustomRepository<E> repository, String serviceName);
}
