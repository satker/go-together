package org.go.together.repository.builders;

import org.go.together.model.IdentifiedEntity;

import java.util.Collection;
import java.util.Optional;

public interface Sql<E extends IdentifiedEntity> extends Query<E> {
    String getQuery();

    Collection<E> fetchAll();

    Optional<E> fetchOne();

    Collection<E> fetchWithPageable(int start, int pageSize);

    Collection<Object> fetchWithPageableNotDefined(int start, int pageSize);

    Collection<Object> fetchAllNotDefined();

    Long getCountRows();
}
