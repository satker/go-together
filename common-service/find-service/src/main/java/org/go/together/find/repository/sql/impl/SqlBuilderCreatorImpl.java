package org.go.together.find.repository.sql.impl;

import org.apache.commons.lang3.StringUtils;
import org.go.together.base.CustomRepository;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.find.repository.sql.interfaces.SqlBuilderCreator;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.query.SqlBuilder;
import org.springframework.stereotype.Component;

import static org.go.together.find.utils.FindUtils.DOT;
import static org.go.together.find.utils.FindUtils.getHavingCondition;

@Component
public class SqlBuilderCreatorImpl<E extends IdentifiedEntity> implements SqlBuilderCreator<E> {
    public SqlBuilder<E> getSqlBuilder(String mainField, CustomRepository<E> repository, String serviceName) {
        String mainKeyToSort = mainField.replaceAll(serviceName + DOT, "");
        if (StringUtils.isNotBlank(mainKeyToSort) && !mainKeyToSort.equals(serviceName)) {
            String[] havingCondition = getHavingCondition(mainKeyToSort);
            if (havingCondition.length > 1) {
                return getHavingSqlBuilder(havingCondition, repository);
            } else {
                return repository.createQuery(mainKeyToSort);
            }
        } else {
            return repository.createQuery();
        }
    }

    private SqlBuilder<E> getHavingSqlBuilder(String[] havingCondition, CustomRepository<E> repository) {
        try {
            int havingNumber = Integer.parseInt(havingCondition[1]);
            return repository.createQuery(havingCondition[0], havingNumber);
        } catch (NumberFormatException e) {
            throw new IncorrectFindObject("Incorrect having condition: " + havingCondition[1]);
        }
    }
}
