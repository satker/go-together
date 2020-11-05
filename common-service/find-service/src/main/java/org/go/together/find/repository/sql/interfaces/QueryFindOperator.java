package org.go.together.find.repository.sql.interfaces;

import org.go.together.enums.FindOperator;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.query.WhereBuilder;

public interface QueryFindOperator {
    void enrichQuery(FindOperator operator,
                     WhereBuilder<? extends IdentifiedEntity> groupWhere,
                     String fieldName,
                     Object searchObject);
}
