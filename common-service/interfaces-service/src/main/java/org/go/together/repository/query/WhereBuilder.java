package org.go.together.repository.query;

import org.go.together.enums.SqlOperator;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.builders.QueryBuilder;
import org.go.together.repository.builders.Where;

import java.util.Map;

public interface WhereBuilder<E extends IdentifiedEntity> extends QueryBuilder<E> {
    void addJoin(Map<String, String> joinMap);

    WhereBuilder<E> condition(String field, SqlOperator sqlOperator, Object value);

    WhereBuilder<E> group(WhereBuilder<E> where);

    WhereBuilder<E> and();

    WhereBuilder<E> or();

    void cutLastAnd();

    void cutLastOr();

    Where<E> build();
}
