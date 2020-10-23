package org.go.together.find.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.correction.fieldpath.FieldPathCorrector;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.dto.form.PageDto;
import org.go.together.find.repository.sql.impl.SqlBuilderCreatorImpl;
import org.go.together.find.repository.sql.interfaces.WhereBuilderCreator;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.builder.Sql;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.SqlBuilder;
import org.go.together.repository.interfaces.WhereBuilder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindRepositoryImpl<E extends IdentifiedEntity> implements FindRepository<E> {
    private final SqlBuilderCreatorImpl<E> sqlBuilderCreator;
    private final FieldPathCorrector fieldPathCorrector;
    private final WhereBuilderCreator<E> whereBuilderCreator;

    @Override
    public Pair<PageDto, Collection<Object>> getResult(FormDto formDto,
                                                       Map<FieldDto, FilterDto> filters,
                                                       String serviceName,
                                                       CustomRepository<E> repository,
                                                       Map<String, FieldMapper> fieldMappers) {
        SqlBuilder<E> query = sqlBuilderCreator.getSqlBuilder(formDto.getMainIdField(), repository, serviceName);

        enrichWhere(filters, query, repository);

        PageDto page = formDto.getPage();
        enrichSort(query, page, fieldMappers);

        Sql<E> sql = query.build();
        long countRows = sql.getCountRows();
        System.out.println(sql.getQuery());
        PageDto pageDto = getPageDto(page, countRows);
        Collection<Object> result = getResult(page, sql);
        return Pair.of(pageDto, result);
    }

    private void enrichSort(SqlBuilder<E> query, PageDto page, Map<String, FieldMapper> fieldMappers) {
        if (page != null) {
            page.getSort().stream()
                    .map(sortDto -> Map.entry(getSortField(sortDto.getField(), fieldMappers), sortDto.getDirection()))
                    .forEach(entry -> query.sort(entry.getKey(), entry.getValue()));
        }
    }

    private void enrichWhere(Map<FieldDto, FilterDto> filters, SqlBuilder<E> query, CustomRepository<E> repository) {
        if (filters != null && !filters.isEmpty()) {
            WhereBuilder<E> where = whereBuilderCreator.getWhereBuilder(filters, repository);
            query.where(where);
        }
    }

    private String getSortField(String field, Map<String, FieldMapper> fieldMappers) {
        FieldDto fieldDto = new FieldDto(field);
        CorrectedFieldDto correctedFieldDto = fieldPathCorrector.getCorrectedFieldDto(fieldDto, fieldMappers);
        return correctedFieldDto.getFieldDto().getLocalField();
    }

    private Collection<Object> getResult(PageDto page, Sql<E> sql) {
        return Optional.ofNullable(page)
                .map(pageDto -> sql.fetchWithPageableNotDefined(page.getPage() * page.getSize(), page.getSize()))
                .orElse(sql.fetchAllNotDefined());
    }

    private PageDto getPageDto(PageDto page, long countRows) {
        return Optional.ofNullable(page).map(pageDto -> new PageDto(pageDto.getPage(),
                pageDto.getSize(), countRows, pageDto.getSort())).orElse(null);
    }
}
