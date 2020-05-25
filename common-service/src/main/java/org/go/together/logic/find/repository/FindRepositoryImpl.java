package org.go.together.logic.find.repository;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.find.enums.FindSqlOperator;
import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.builder.SqlBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

public class FindRepositoryImpl<E extends IdentifiedEntity> implements FindRepository {
    private final String serviceName;
    private final CustomRepository<E> repository;

    public FindRepositoryImpl(String serviceName, CustomRepository<E> repository) {
        this.serviceName = serviceName;
        this.repository = repository;
    }

    @Override
    public Pair<PageDto, Collection<Object>> getResult(String mainField, Map<String, FilterDto> filters, PageDto page) {
        String mainKeyToSort = mainField.replaceAll(serviceName + "\\.", "");
        SqlBuilder<E> query;
        if (StringUtils.isNotBlank(mainKeyToSort) && !mainKeyToSort.equals(serviceName)) {
            query = repository.createQuery(mainKeyToSort);
        } else {
            query = repository.createQuery();
        }
        long countRows;
        if (filters == null || filters.isEmpty()) {
            countRows = (long) query.getCountRows();
        } else {
            SqlBuilder<E>.WhereBuilder whereBuilder = repository.createWhere();
            // get current main key
            filters.forEach((key, value) -> {
                FindSqlOperator filterType = value.getFilterType();
                Collection<SimpleDto> values = value.getValues();
                Function<Collection<SimpleDto>, Object> dtosMapper = filterType.getSearchObjectFromDtos();
                whereBuilder.condition(key, filterType.getOperator(), dtosMapper.apply(values)).and();
            });
            whereBuilder.cutLastAnd();
            query.where(whereBuilder);
            countRows = (long) repository.createQuery().getCountRowsWhere(whereBuilder);
        }
        PageDto pageDto = null;
        Collection<Object> result;
        if (page != null) {
            pageDto = new PageDto(page.getPage(), page.getSize(), countRows, page.getSort());
            result = query.fetchWithPageableNotDefined(page.getPage() * page.getSize(), page.getSize());
        } else {
            result = query.fetchAllNotDefined();
        }
        return Pair.of(pageDto, result);
    }
}
