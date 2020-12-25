package org.go.together.find.repository.sql.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.query.SqlBuilder;

public interface SqlBuilderCreator<E extends IdentifiedEntity> {
    SqlBuilder<E> getSqlBuilder(String mainField, CustomRepository<E> repository, String serviceName);
}
