package org.go.together.find.repository.sql.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.go.together.base.CustomRepository;
import org.go.together.dto.form.FilterDto;
import org.go.together.enums.FindOperator;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.repository.sql.interfaces.QueryFindOperator;
import org.go.together.find.repository.sql.interfaces.WhereBuilderCreator;
import org.go.together.find.utils.FindUtils;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.query.WhereBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.go.together.find.utils.FindUtils.getSingleGroupFields;

@Component
@RequiredArgsConstructor
public class WhereBuilderCreatorImpl<E extends IdentifiedEntity> implements WhereBuilderCreator<E> {
    private final QueryFindOperator queryFindOperator;

    public WhereBuilder<E> getWhereBuilder(Map<FieldDto, FilterDto> filters, CustomRepository<E> repository) {
        Map<String, String> joinMap = new HashMap<>();
        WhereBuilder<E> where = repository.createWhere();
        filters.entrySet().parallelStream().map(entry -> createWhereByFilter(entry, repository, joinMap))
                .forEach(createdWhere -> where.group(createdWhere).and());
        where.cutLastAnd();
        if (!joinMap.isEmpty()) {
            where.addJoin(joinMap);
        }
        return where;
    }

    private WhereBuilder<E> createWhereByFilter(Map.Entry<FieldDto,
            FilterDto> entry,
                                                CustomRepository<E> repository,
                                                Map<String, String> joinMap) {
        String searchField = entry.getKey().getFilterFields();
        String[] groupFields = getSingleGroupFields(searchField);
        String suffix = entry.getKey().getLocalField().replace(searchField, "");
        return addGroups(suffix, searchField, entry.getValue(), groupFields, repository, joinMap);
    }

    private WhereBuilder<E> addGroups(String suffix,
                                      String key,
                                      FilterDto filterDto,
                                      String[] groupFields,
                                      CustomRepository<E> repository,
                                      Map<String, String> joinMap) {
        WhereBuilder<E> groupWhere = repository.createWhere();
        filterDto.getValues().forEach(filterValues -> {
            WhereBuilder<E> whereAdd = filterDto.getValues().size() > 1 ? repository.createWhere() : groupWhere;
            Stream.of(groupFields).forEach(field -> {
                String currentGroupField = field;
                if (StringUtils.isNotBlank(suffix)) {
                    currentGroupField = suffix + field;
                }
                addCondition(filterValues, filterDto.getFilterType(), whereAdd, currentGroupField);
                if (groupFields.length > 1) {
                    addDelimiter(key, whereAdd, field);
                }
            });
            if (filterDto.getValues().size() > 1) {
                groupWhere.group(whereAdd).or();
            }
            joinMap.putAll(whereAdd.build().getJoin());
        });
        groupWhere.cutLastOr();
        return groupWhere;
    }

    private void addDelimiter(String key, WhereBuilder<E> groupWhere, String field) {
        Optional.ofNullable(FindUtils.getDelimiter(key, field))
                .ifPresent(delimiter -> {
                    if (delimiter.equals(FindUtils.GROUP_AND)) {
                        groupWhere.and();
                    } else if (delimiter.equals(FindUtils.GROUP_OR)) {
                        groupWhere.or();
                    }
                });
    }

    private void addCondition(Map<String, Object> value,
                              FindOperator filterType,
                              WhereBuilder<E> groupWhere,
                              String field) {
        Object searchObject = Optional.ofNullable(value.get(field))
                .orElseGet(() -> {
                    String[] searchField = FindUtils.getParsedFields(field);
                    return value.get(searchField[searchField.length - 1]);
                });
        queryFindOperator.enrichQuery(filterType, groupWhere, field, searchObject);
    }
}
