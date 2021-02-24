package org.go.together.find.repository.sql.impl;

import org.go.together.enums.FindOperator;
import org.go.together.enums.SqlOperator;
import org.go.together.find.repository.sql.interfaces.QueryFindOperator;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.query.WhereBuilder;
import org.springframework.stereotype.Component;

@Component
public class QueryFindOperatorImpl implements QueryFindOperator {
    @Override
    public void enrichQuery(FindOperator operator,
                            WhereBuilder<? extends IdentifiedEntity> queryBuilder,
                            String fieldName,
                            Object searchObject) {
        switch (operator) {
            case IN -> queryBuilder.condition(fieldName, SqlOperator.IN, searchObject);
            case LIKE -> queryBuilder.condition(fieldName, SqlOperator.LIKE, searchObject);
            case EQUAL -> queryBuilder.condition(fieldName, SqlOperator.EQUAL, searchObject);
            case END_DATE -> queryBuilder.condition(fieldName, SqlOperator.LESS_OR_EQUAL, searchObject);
            case START_DATE -> queryBuilder.condition(fieldName, SqlOperator.GREATER_OR_EQUAL, searchObject);
            case NEAR_LOCATION -> queryBuilder.condition(fieldName, SqlOperator.NEAR_LOCATION, searchObject);
        }
    }
}
