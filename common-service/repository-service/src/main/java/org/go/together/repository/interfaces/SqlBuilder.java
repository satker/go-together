package org.go.together.repository.interfaces;

import org.go.together.repository.builder.Sql;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Map;

public interface SqlBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    SqlBuilder<E> having(Integer havingCondition);

    SqlBuilder<E> select(String selectRow);

    SqlBuilder<E> where(WhereBuilder<E> where);

    SqlBuilder<E> sort(Map<String, Direction> sortMap);

    Sql<E> build();
}
