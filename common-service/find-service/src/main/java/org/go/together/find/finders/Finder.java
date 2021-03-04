package org.go.together.find.finders;

import org.go.together.compare.FieldMapper;
import org.go.together.find.dto.node.FilterNode;

import java.util.Collection;

public interface Finder {
    Collection<Object> getFilters(FilterNode filters,
                                  FieldMapper fieldMapper);
}
