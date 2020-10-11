package org.go.together.find.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.correction.fieldpath.FieldPathCorrector;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.dto.form.PageDto;
import org.go.together.find.repository.sql.SqlBuilderCreator;
import org.go.together.find.repository.sql.WhereBuilderCreator;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.builder.SqlBuilder;
import org.go.together.repository.builder.Where;
import org.go.together.repository.builder.dto.SqlDto;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class FindRepositoryImpl<E extends IdentifiedEntity> implements FindRepository {
    private final SqlBuilderCreator<E> sqlBuilderCreator;
    private final WhereBuilderCreator<E> whereBuilderCreator;
    private final CustomRepository<E> repository;
    private final Map<String, FieldMapper> fieldMappers;
    private final FieldPathCorrector fieldPathCorrector;

    public FindRepositoryImpl(String serviceName,
                              CustomRepository<E> repository,
                              Map<String, FieldMapper> fieldMappers,
                              FieldPathCorrector fieldPathCorrector) {
        this.sqlBuilderCreator = new SqlBuilderCreator<>(repository, serviceName);
        this.whereBuilderCreator = new WhereBuilderCreator<>(repository);
        this.repository = repository;
        this.fieldMappers = fieldMappers;
        this.fieldPathCorrector = fieldPathCorrector;
    }

    @Override
    public Pair<PageDto, Collection<Object>> getResult(FormDto formDto,
                                                       Map<FieldDto, FilterDto> filters) {
        SqlBuilder<E> query = sqlBuilderCreator.getSqlBuilder(formDto.getMainIdField());
        long countRows;
        if (filters == null || filters.isEmpty()) {
            countRows = (long) query.getCountRows();
        } else {
            Where.WhereBuilder where = whereBuilderCreator.getWhereBuilder(filters);
            query.where(where);
            SqlDto buildSql = query.build();
            countRows = (long) repository.createQuery().getCountRowsWhere(where,
                    buildSql.getSelectRow(),
                    buildSql.getHavingCondition());
        }
        PageDto page = formDto.getPage();
        Map<String, Direction> sortMappers = getSortMappers(page);
        if (sortMappers != null && !sortMappers.isEmpty()) {
            query.sort(sortMappers);
        }
        PageDto pageDto = getPageDto(page, countRows);
        System.out.println(query.build().getQuery());
        Collection<Object> result = getResult(page, query);
        return Pair.of(pageDto, result);
    }

    private Map<String, Direction> getSortMappers(PageDto pageDto) {
        if (pageDto == null) {
            return null;
        }

        return pageDto.getSort().stream()
                .map(sortDto -> Map.entry(correctedField(sortDto.getField()), sortDto.getDirection()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private String correctedField(String field) {
        FieldDto fieldDto = new FieldDto(field);
        CorrectedFieldDto correctedFieldDto = fieldPathCorrector.getCorrectedFieldDto(fieldDto, fieldMappers);
        return correctedFieldDto.getFieldDto().getLocalField();
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
}
