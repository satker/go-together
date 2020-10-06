package org.go.together.find.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.dto.form.PageDto;

import java.util.Collection;
import java.util.Map;

public interface FindRepository {
    Pair<PageDto, Collection<Object>> getResult(FormDto formDto,
                                                Map<FieldDto, FilterDto> filters);
}
