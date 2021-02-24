package org.go.together.find.finders;

import org.go.together.compare.FieldMapper;
import org.go.together.find.dto.node.FilterNode;

import java.util.Collection;
import java.util.UUID;

public interface Finder {
    Collection<Object> getFilters(UUID requestId,
                                  FilterNode filters,
                                  FieldMapper fieldMapper);
}
