package org.go.together.logic.find.repository;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.find.enums.FindSqlOperator;
import org.go.together.logic.find.utils.FieldParser;
import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.builder.SqlBuilder;
import org.go.together.logic.repository.builder.WhereBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.go.together.logic.find.utils.FieldParser.getSingleGroupFields;

public class FindRepositoryImpl<E extends IdentifiedEntity> implements FindRepository {
    private final String serviceName;
    private final CustomRepository<E> repository;
    private final StringBuilder join = new StringBuilder();

    public FindRepositoryImpl(String serviceName, CustomRepository<E> repository) {
        this.serviceName = serviceName;
        this.repository = repository;
    }

    @Override
    public Pair<PageDto, Collection<Object>> getResult(String mainField, Map<String, FilterDto> filters, PageDto page) {
        SqlBuilder<E> query = getSqlBuilder(mainField);
        long countRows;
        if (filters == null || filters.isEmpty()) {
            countRows = (long) query.getCountRows();
        } else {
            WhereBuilder<E> whereBuilder = getWhereBuilder(filters);
            query.where(whereBuilder);
            countRows = (long) repository.createQuery().getCountRowsWhere(whereBuilder);
        }
        PageDto pageDto = getPageDto(page, countRows);
        Collection<Object> result = getResult(page, query);
        return Pair.of(pageDto, result);
    }

    private SqlBuilder<E> getSqlBuilder(String mainField) {
        String mainKeyToSort = mainField.replaceAll(serviceName + "\\.", "");

        SqlBuilder<E> query;
        if (StringUtils.isNotBlank(mainKeyToSort) && !mainKeyToSort.equals(serviceName)) {
            query = repository.createQuery(mainKeyToSort);
        } else {
            query = repository.createQuery();
        }
        return query;
    }

    private Collection<Object> getResult(PageDto page, SqlBuilder<E> query) {
        Collection<Object> result;
        if (page != null) {
            result = query.fetchWithPageableNotDefined(page.getPage() * page.getSize(), page.getSize());
        } else {
            result = query.fetchAllNotDefined();
        }
        return result;
    }

    private PageDto getPageDto(PageDto page, long countRows) {
        PageDto pageDto = null;
        if (page != null) {
            pageDto = new PageDto(page.getPage(), page.getSize(), countRows, page.getSort());
        }
        return pageDto;
    }

    private WhereBuilder<E> getWhereBuilder(Map<String, FilterDto> filters) {
        WhereBuilder<E> whereBuilder = repository.createWhere();
        // get current main key
        // TODO: think about dilimiter realization | &
        filters.forEach((key, value) -> {
            FindSqlOperator filterType = value.getFilterType();
            BiConsumer<Pair<String, Object>, WhereBuilder> searchObjectFromDtos =
                    filterType.getSearchObjectFromDtos();
            String[] groupFields = getSingleGroupFields(key);
            if (groupFields.length > 1) {
                WhereBuilder<E> groupWhere = repository.createGroup();
                addGroups(key, value, searchObjectFromDtos, groupFields, groupWhere);
                whereBuilder.group(groupWhere).and();
            } else if (groupFields.length == 1) {
                value.getValues().forEach(map -> {
                    WhereBuilder<E> groupWhere = repository.createGroup();
                    addCondition(map, searchObjectFromDtos, groupWhere, groupFields[0]);
                    join.append(groupWhere.getJoinQuery());
                    whereBuilder.group(groupWhere).and();
                });
            }
        });

        whereBuilder.cutLastAnd();
        if (StringUtils.isNotBlank(join)) {
            whereBuilder.addJoin(join);
        }
        return whereBuilder;
    }

    private void addGroups(String key, FilterDto value,
                           BiConsumer<Pair<String, Object>, WhereBuilder> searchObjectFromDtos,
                           String[] groupFields,
                           WhereBuilder<E> groupWhere) {
        value.getValues()
                .forEach(map -> {
                    WhereBuilder<E> innerGroup = repository.createGroup();
                    Stream.of(groupFields)
                            .forEach(field -> {
                                WhereBuilder<E> whereAdd = value.getValues().size() > 1 ? innerGroup : groupWhere;
                                addCondition(map, searchObjectFromDtos, whereAdd, field);
                                addDelimiter(key, whereAdd, field);
                            });
                    if (value.getValues().size() > 1) {
                        groupWhere.group(innerGroup).or();
                    }
                    join.append(innerGroup.getJoinQuery());
                });
        groupWhere.cutLastOr();
    }

    private void addDelimiter(String key, WhereBuilder<E> groupWhere, String field) {
        String delimiter = FieldParser.getDelimiter(key, field);
        if (delimiter != null) {
            if (delimiter.equals(FieldParser.GROUP_AND)) {
                groupWhere.and();
            } else {
                groupWhere.or();
            }
        }
    }

    private void addCondition(Map<String, Object> value,
                              BiConsumer<Pair<String, Object>, WhereBuilder> searchObjectFromDtos,
                              WhereBuilder<E> groupWhere, String field) {
        String[] searchField = FieldParser.getSplitByDotString(field);
        Object searchObject = value.get(searchField[searchField.length - 1]);
        Pair<String, Object> searchPair = Pair.of(field, searchObject);
        searchObjectFromDtos.accept(searchPair, groupWhere);
    }
}
