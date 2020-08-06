package org.go.together.find.repository;

import org.apache.commons.lang3.StringUtils;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.builder.SqlBuilder;

import static org.go.together.find.utils.FindUtils.DOT;
import static org.go.together.find.utils.FindUtils.getHavingCondition;

public class SqlBuilderCreator<E extends IdentifiedEntity> {
    private final CustomRepository<E> repository;
    private final String serviceName;

    public SqlBuilderCreator(CustomRepository<E> repository, String serviceName) {
        this.repository = repository;
        this.serviceName = serviceName;
    }

    public SqlBuilder<E> getSqlBuilder(String mainField) {
        String mainKeyToSort = mainField.replaceAll(serviceName + DOT, "");
        if (StringUtils.isNotBlank(mainKeyToSort) && !mainKeyToSort.equals(serviceName)) {
            String[] havingCondition = getHavingCondition(mainKeyToSort);
            if (havingCondition.length > 1) {
                return getHavingSqlBuilder(havingCondition);
            } else {
                return repository.createQuery(mainKeyToSort, null);
            }
        } else {
            return repository.createQuery();
        }
    }

    private SqlBuilder<E> getHavingSqlBuilder(String[] havingCondition) {
        try {
            int havingNumber = Integer.parseInt(havingCondition[1]);
            return repository.createQuery(havingCondition[0], havingNumber);
        } catch (NumberFormatException e) {
            throw new IncorrectFindObject("Incorrect having condition: " + havingCondition[1]);
        }
    }
}
