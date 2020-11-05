package org.go.together.repository.query;

import org.go.together.enums.Direction;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.builders.QueryBuilder;
import org.go.together.repository.builders.Sql;

public interface SqlBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    SqlBuilder<E> having(Integer havingCondition);

    SqlBuilder<E> select(String selectRow);

    SqlBuilder<E> where(WhereBuilder<E> where);

    SqlBuilder<E> sort(String key, Direction direction);

    Sql<E> build();
}
