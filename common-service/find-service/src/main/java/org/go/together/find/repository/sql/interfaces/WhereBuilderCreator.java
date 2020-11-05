package org.go.together.find.repository.sql.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.dto.form.FilterDto;
import org.go.together.find.dto.FieldDto;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.query.WhereBuilder;

import java.util.Map;

public interface WhereBuilderCreator<E extends IdentifiedEntity> {
    WhereBuilder<E> getWhereBuilder(Map<FieldDto, FilterDto> filters, CustomRepository<E> repository);
}
