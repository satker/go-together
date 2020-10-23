package org.go.together.find.repository.sql.interfaces;

import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.repository.CustomRepository;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.WhereBuilder;

import java.util.Map;

public interface WhereBuilderCreator<E extends IdentifiedEntity> {
    WhereBuilder<E> getWhereBuilder(Map<FieldDto, FilterDto> filters, CustomRepository<E> repository);
}
