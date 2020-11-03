package org.go.together.repository.interfaces;

import org.go.together.enums.Direction;
import org.go.together.repository.builder.Sql;
import org.go.together.repository.entities.IdentifiedEntity;

public interface SqlBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    SqlBuilder<E> having(Integer havingCondition);

    SqlBuilder<E> select(String selectRow);

    SqlBuilder<E> where(WhereBuilder<E> where);

    SqlBuilder<E> sort(String key, Direction direction);

    Sql<E> build();
}
