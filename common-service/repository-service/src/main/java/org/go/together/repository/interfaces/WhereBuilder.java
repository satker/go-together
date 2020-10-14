package org.go.together.repository.interfaces;

import org.go.together.repository.builder.Where;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.sql.SqlOperator;

public interface WhereBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    void addJoin(StringBuilder join);

    WhereBuilder<E> condition(String field, SqlOperator sqlOperator, Object value);

    WhereBuilder<E> group(WhereBuilder<E> where);

    WhereBuilder<E> and();

    WhereBuilder<E> or();

    void cutLastAnd();

    void cutLastOr();

    Where build();
}
