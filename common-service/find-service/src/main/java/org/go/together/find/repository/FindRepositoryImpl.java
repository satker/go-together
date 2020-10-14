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
import org.go.together.repository.builder.interfaces.SqlBuilder;
import org.go.together.repository.builder.interfaces.WhereBuilder;
import org.go.together.repository.builder.query.Sql;
import org.go.together.repository.entities.Direction;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
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
            countRows = query.build().getCountRows();
        } else {
            WhereBuilder<E> where = whereBuilderCreator.getWhereBuilder(filters);
            query.where(where);
            countRows = query.build().getCountRows();
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
        Sql<E> buildSql = query.build();
        return Optional.ofNullable(page)
                .map(pageDto -> buildSql.fetchWithPageableNotDefined(page.getPage() * page.getSize(), page.getSize()))
                .orElse(buildSql.fetchAllNotDefined());
    }

    private PageDto getPageDto(PageDto page, long countRows) {
        return Optional.ofNullable(page).map(pageDto -> new PageDto(pageDto.getPage(),
                pageDto.getSize(), countRows, pageDto.getSort())).orElse(null);
    }
}
