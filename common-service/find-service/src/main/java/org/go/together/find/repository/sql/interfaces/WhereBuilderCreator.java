package org.go.together.find.repository.sql.interfaces;

import org.go.together.base.CustomRepository;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.query.WhereBuilder;

import java.util.Collection;

public interface WhereBuilderCreator<E extends IdentifiedEntity> {
    WhereBuilder<E> create(Collection<FilterNodeBuilder> builders, CustomRepository<E> repository);
}
