package org.go.together.find.repository;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.go.together.base.CustomRepository;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.dto.PageDto;
import org.go.together.find.correction.path.PathCorrector;
import org.go.together.find.dto.Field;
import org.go.together.find.dto.Path;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.go.together.find.repository.sql.impl.SqlBuilderCreatorImpl;
import org.go.together.find.repository.sql.interfaces.WhereBuilderCreator;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.builders.Sql;
import org.go.together.repository.query.SqlBuilder;
import org.go.together.repository.query.WhereBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FindRepositoryImpl<E extends IdentifiedEntity> implements FindRepository<E> {
    private static final Logger log = LoggerFactory.getLogger(FindRepositoryImpl.class);

    private final SqlBuilderCreatorImpl<E> sqlBuilderCreator;
    private final PathCorrector pathCorrector;
    private final WhereBuilderCreator<E> whereBuilderCreator;

    @Override
    public Pair<PageDto, Collection<Object>> getResult(FormDto formDto,
                                                       Collection<Collection<FilterNodeBuilder>> filters,
                                                       String serviceName,
                                                       CustomRepository<E> repository,
                                                       Map<String, FieldMapper> fieldMappers) {
        SqlBuilder<E> query = sqlBuilderCreator.getSqlBuilder(formDto.getMainIdField(), repository, serviceName);

        enrichWhere(filters, query, repository);

        PageDto page = formDto.getPage();
        enrichSort(query, page, fieldMappers);

        Sql<E> sql = query.build();
        long countRows = sql.getCountRows();

        log.info("Find query: {}", sql.getQuery());

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

    private void enrichWhere(Collection<Collection<FilterNodeBuilder>> filters, SqlBuilder<E> query, CustomRepository<E> repository) {
        if (filters != null && !filters.isEmpty()) {
            WhereBuilder<E> where = repository.createWhere();
            filters.stream()
                    .map(filter -> whereBuilderCreator.create(filter, repository))
                    .forEach(whereBuilder -> where.group(whereBuilder).and());
            where.cutLastAnd();
            query.where(where);
        }
    }

    private String getSortField(String field, Map<String, FieldMapper> fieldMappers) {
        Field fieldDto = new Field(field);
        Path path = pathCorrector.correct(fieldDto, fieldMappers);
        return path.getField().getLocalField();
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
