package org.go.together.logic.find.repository;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.filter.FilterDto;
import org.go.together.dto.filter.PageDto;
import org.go.together.exceptions.IncorrectFindObject;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.find.enums.FindSqlOperator;
import org.go.together.logic.find.utils.FieldParser;
import org.go.together.logic.repository.CustomRepository;
import org.go.together.logic.repository.builder.SqlBuilder;
import org.go.together.logic.repository.builder.WhereBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Stream;

import static org.go.together.logic.find.utils.FieldParser.getSingleGroupFields;
import static org.go.together.logic.find.utils.FieldParser.getSplitHavingCountString;

public class FindRepositoryImpl<E extends IdentifiedEntity> implements FindRepository {
    private final String serviceName;
    private final CustomRepository<E> repository;
    private final StringBuilder join;

    public FindRepositoryImpl(String serviceName, CustomRepository<E> repository) {
        this.serviceName = serviceName;
        this.repository = repository;
        join = new StringBuilder();
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
            countRows = (long) repository.createQuery().getCountRowsWhere(whereBuilder, query.getSelectRow(), query.getHaving());
        }
        PageDto pageDto = getPageDto(page, countRows);
        Collection<Object> result = getResult(page, query);
        return Pair.of(pageDto, result);
    }

    private SqlBuilder<E> getSqlBuilder(String mainField) {
        String mainKeyToSort = mainField.replaceAll(serviceName + "\\.", "");

        SqlBuilder<E> query;
        if (StringUtils.isNotBlank(mainKeyToSort) && !mainKeyToSort.equals(serviceName)) {
            String[] havingCondition = getSplitHavingCountString(mainKeyToSort);
            if (havingCondition.length > 1) {
                try {
                    int havingNumber = Integer.parseInt(havingCondition[1]);
                    query = repository.createQuery(havingCondition[0], havingNumber);
                } catch (NumberFormatException e) {
                    throw new IncorrectFindObject("Incorrect having condition: " + havingCondition[1]);
                }
            } else {
                query = repository.createQuery(mainKeyToSort, null);
            }
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
        filters.forEach((key, value) -> {
            FindSqlOperator filterType = value.getFilterType();
            String[] splitByDotString = FieldParser.getPathFields(key);
            String searchField = splitByDotString[splitByDotString.length - 1];
            String[] groupFields = getSingleGroupFields(searchField);
            String suffix = key.replace(searchField, "");
            if (groupFields.length > 1) {
                WhereBuilder<E> groupWhere = repository.createGroup();
                addGroups(suffix, searchField, value.getValues(), filterType, groupFields, groupWhere);
                whereBuilder.group(groupWhere).and();
            } else if (groupFields.length == 1) {
                value.getValues().forEach(map -> {
                    WhereBuilder<E> groupWhere = repository.createGroup();
                    String field = groupFields[0];
                    if (StringUtils.isNotBlank(suffix)) {
                        field = suffix + field;
                    }
                    addCondition(map, filterType, groupWhere, field);
                    if (!join.toString().contains(groupWhere.getJoinQuery())) {
                        join.append(groupWhere.getJoinQuery());
                    }
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

    private void addGroups(String suffix,
                           String key,
                           Collection<Map<String, Object>> values,
                           FindSqlOperator filterType,
                           String[] groupFields,
                           WhereBuilder<E> groupWhere) {
        values.forEach(map -> {
            WhereBuilder<E> innerGroup = repository.createGroup();
            Stream.of(groupFields).forEach(field -> {
                WhereBuilder<E> whereAdd = values.size() > 1 ? innerGroup : groupWhere;
                String currentGroupField = field;
                if (StringUtils.isNotBlank(suffix)) {
                    currentGroupField = suffix + field;
                }
                addCondition(map, filterType, whereAdd, currentGroupField);
                addDelimiter(key, whereAdd, field);
            });
            if (values.size() > 1) {
                if (!join.toString().contains(innerGroup.getJoinQuery())) {
                    join.append(innerGroup.getJoinQuery());
                }
                groupWhere.group(innerGroup).or();
            } else {
                if (!join.toString().contains(groupWhere.getJoinQuery())) {
                    join.append(groupWhere.getJoinQuery());
                }
            }
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
                              FindSqlOperator filterType,
                              WhereBuilder<E> groupWhere, String field) {
        Object searchObject = value.get(field);
        if (searchObject == null) {
            String[] searchField = FieldParser.getSplitByDotString(field);
            searchObject = value.get(searchField[searchField.length - 1]);
        }
        Pair<String, Object> searchPair = Pair.of(field, searchObject);
        filterType.getSearchObjectFromDtos().accept(searchPair, groupWhere);
    }
}
