package org.go.together.logic.find;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.SimpleDto;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.FilterSqlOperator;
import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.utils.sql.CustomSqlBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class LocalFindService<E extends IdentifiedEntity> {
    private final CustomRepository<E> repository;

    protected LocalFindService(CustomRepository<E> repository) {
        this.repository = repository;
    }

    public abstract String getServiceName();

    public abstract Map<String, FieldMapper> getMappingFields();

    protected Map<String, FilterDto> enrichFilterByFoundedValues(Map<String, Collection<Object>> foundedKeysFromAnotherService,
                                                                 Map<String, FilterDto> currentServiceFilters) {
        if (foundedKeysFromAnotherService.isEmpty()) {
            return currentServiceFilters;
        }
        foundedKeysFromAnotherService.forEach((key, values) -> {
            Set<SimpleDto> valuesFromOtherService = values.stream()
                    .map(value -> new SimpleDto(String.valueOf(value), String.valueOf(value)))
                    .collect(Collectors.toSet());

            FilterDto filterDto = new FilterDto(FilterSqlOperator.IN, valuesFromOtherService);
            Map<String, FilterDto> filterForCurrentService = Collections.singletonMap(key, filterDto);
            currentServiceFilters.putAll(filterForCurrentService);
        });

        return currentServiceFilters;
    }

    // get values
    protected Pair<PageDto, Collection<Object>> getValuesInCurrentService(String mainField, Map<String, FilterDto> filters, PageDto page) {
        String mainKeyToSort = mainField.replaceAll(getServiceName() + "\\.", "");
        CustomSqlBuilder<E> query;
        if (StringUtils.isNotBlank(mainKeyToSort) && !mainKeyToSort.equals(getServiceName())) {
            query = repository.createQuery(mainKeyToSort);
        } else {
            query = repository.createQuery();
        }
        long countRows;
        if (filters == null || filters.isEmpty()) {
            countRows = (long) query.getCountRows();
        } else {
            CustomSqlBuilder<E>.WhereBuilder whereBuilder = repository.createWhere();
            // get current main key
            filters.forEach((key, value) -> {
                FilterSqlOperator filterType = value.getFilterType();
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
