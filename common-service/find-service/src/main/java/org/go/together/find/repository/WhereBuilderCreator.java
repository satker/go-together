package org.go.together.find.repository;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.dto.FilterDto;
import org.go.together.find.dto.FindSqlOperator;
import org.go.together.find.utils.FindUtils;
import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.builder.WhereBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.go.together.find.utils.FindUtils.getSingleGroupFields;

public class WhereBuilderCreator<E extends IdentifiedEntity> {
    private final CustomRepository<E> repository;
    private final StringBuilder join;

    public WhereBuilderCreator(CustomRepository<E> repository) {
        this.repository = repository;
        this.join = new StringBuilder();
    }

    public WhereBuilder<E> getWhereBuilder(Map<String, FilterDto> filters) {
        WhereBuilder<E> whereBuilder = repository.createWhere();
        filters.forEach((key, value) -> {
            FindSqlOperator filterType = value.getFilterType();
            String[] splitByDotString = FindUtils.getPathFields(key);
            String searchField = splitByDotString[splitByDotString.length - 1];
            String[] groupFields = getSingleGroupFields(searchField);
            String suffix = key.replace(searchField, "");
            if (groupFields.length > 1) {
                WhereBuilder<E> groupWhere = addGroups(suffix, searchField, value, groupFields);
                whereBuilder.group(groupWhere).and();
            } else if (groupFields.length == 1) {
                value.getValues().forEach(filterValues -> {
                    WhereBuilder<E> groupWhere = repository.createGroup();
                    String field = groupFields[0];
                    if (StringUtils.isNotBlank(suffix)) {
                        field = suffix + field;
                    }
                    addCondition(filterValues, filterType, groupWhere, field);
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

    private WhereBuilder<E> addGroups(String suffix,
                                      String key,
                                      FilterDto filterDto,
                                      String[] groupFields) {
        WhereBuilder<E> groupWhere = repository.createGroup();
        filterDto.getValues().forEach(filterValues -> {
            WhereBuilder<E> innerGroup = repository.createGroup();
            WhereBuilder<E> whereAdd = filterDto.getValues().size() > 1 ? innerGroup : groupWhere;
            Stream.of(groupFields).forEach(field -> {
                String currentGroupField = field;
                if (StringUtils.isNotBlank(suffix)) {
                    currentGroupField = suffix + field;
                }
                addCondition(filterValues, filterDto.getFilterType(), whereAdd, currentGroupField);
                addDelimiter(key, whereAdd, field);
            });
            if (filterDto.getValues().size() > 1) {
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
        return groupWhere;
    }

    private void addDelimiter(String key, WhereBuilder<E> groupWhere, String field) {
        String delimiter = FindUtils.getDelimiter(key, field);
        if (delimiter != null) {
            if (delimiter.equals(FindUtils.GROUP_AND)) {
                groupWhere.and();
            } else {
                groupWhere.or();
            }
        }
    }

    private void addCondition(Map<String, Object> value,
                              FindSqlOperator filterType,
                              WhereBuilder<E> groupWhere, String field) {
        Object searchObject = Optional.ofNullable(value.get(field)).orElseGet(() -> {
            String[] searchField = FindUtils.getParsedFields(field);
            return value.get(searchField[searchField.length - 1]);
        });
        Pair<String, Object> searchPair = Pair.of(field, searchObject);
        filterType.getSearchObjectFromDtos().accept(searchPair, groupWhere);
    }
}
