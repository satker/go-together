package org.go.together.find.repository.sql.impl;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.utils.FindSqlOperator;
import org.go.together.find.repository.sql.interfaces.WhereBuilderCreator;
import org.go.together.find.utils.FindUtils;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.WhereBuilder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.go.together.find.utils.FindUtils.getSingleGroupFields;

@Component
public class WhereBuilderCreatorImpl<E extends IdentifiedEntity> implements WhereBuilderCreator<E> {
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
                              FindSqlOperator filterType,
                              WhereBuilder<E> groupWhere,
                              String field) {
        Object searchObject = Optional.ofNullable(value.get(field))
                .orElseGet(() -> {
                    String[] searchField = FindUtils.getParsedFields(field);
                    return value.get(searchField[searchField.length - 1]);
                });
        Pair<String, Object> searchPair = Pair.of(field, searchObject);
        filterType.getSearchObjectFromDtos().accept(searchPair, groupWhere);
    }
}
