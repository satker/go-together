package org.go.together.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.dto.FilterDto;
import org.go.together.dto.PageDto;

import java.util.Collection;
import java.util.Map;

public interface FindRepository {
    Pair<PageDto, Collection<Object>> getResult(String mainField, Map<String, FilterDto> filters, PageDto page);
}
