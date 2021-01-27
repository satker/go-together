package org.go.together.find.builder;

import org.go.together.dto.FilterDto;
import org.go.together.find.dto.node.FilterNodeBuilder;

import java.util.Collection;
import java.util.Map;

public interface FilterBuilder {
    Collection<FilterNodeBuilder> getBuilders(Map.Entry<String, FilterDto> filter);
}
