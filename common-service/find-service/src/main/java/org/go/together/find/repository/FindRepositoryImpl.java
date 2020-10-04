package org.go.together.find.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.PageDto;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.builder.SqlBuilder;
import org.go.together.repository.builder.WhereBuilder;
import org.go.together.repository.entities.IdentifiedEntity;

import java.util.Collection;
import java.util.Map;

public class FindRepositoryImpl<E extends IdentifiedEntity> implements FindRepository {
    private final SqlBuilderCreator<E> sqlBuilderCreator;
    private final WhereBuilderCreator<E> whereBuilderCreator;
    private final CustomRepository<E> repository;

    public FindRepositoryImpl(String serviceName, CustomRepository<E> repository) {
        this.sqlBuilderCreator = new SqlBuilderCreator<>(repository, serviceName);
        this.whereBuilderCreator = new WhereBuilderCreator<>(repository);
        this.repository = repository;
    }

    @Override
    public Pair<PageDto, Collection<Object>> getResult(String mainField, Map<FieldDto, FilterDto> filters, PageDto page) {
        SqlBuilder<E> query = sqlBuilderCreator.getSqlBuilder(mainField);
        long countRows;
        if (filters == null || filters.isEmpty()) {
            countRows = (long) query.getCountRows();
        } else {
            WhereBuilder<E> whereBuilder = whereBuilderCreator.getWhereBuilder(filters);
            query.where(whereBuilder);
            countRows = (long) repository.createQuery().getCountRowsWhere(whereBuilder, query.getSelectRow(), query.getHaving());
        }
        PageDto pageDto = getPageDto(page, countRows);
        System.out.println(query.getQuery());
        Collection<Object> result = getResult(page, query);
        return Pair.of(pageDto, result);
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
