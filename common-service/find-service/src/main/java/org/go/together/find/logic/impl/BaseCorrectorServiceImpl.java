package org.go.together.find.logic.impl;

import org.go.together.find.correction.CorrectorService;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.FieldMapper;
import org.go.together.find.dto.form.FilterDto;
import org.go.together.find.dto.form.FormDto;
import org.go.together.find.finders.Finder;
import org.go.together.find.logic.interfaces.BaseCorrectorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.go.together.find.utils.FindUtils.mergeFilters;

@Component
public class BaseCorrectorServiceImpl implements BaseCorrectorService {
    private final Finder remoteFindService;
    private final CorrectorService correctorRemoteFiltersService;
    private final CorrectorService correctorLocalFiltersService;

    public BaseCorrectorServiceImpl(Finder remoteFindService,
                                    @Qualifier("remoteFilters") CorrectorService correctorRemoteFiltersService,
                                    @Qualifier("localFilters") CorrectorService correctorLocalFiltersService) {
        this.remoteFindService = remoteFindService;
        this.correctorRemoteFiltersService = correctorRemoteFiltersService;
        this.correctorLocalFiltersService = correctorLocalFiltersService;
    }


    @Override
    public Map<FieldDto, FilterDto> getCorrectedFilters(FormDto formDto, Map<String, FieldMapper> mappingFields) {
        Map<FieldDto, FilterDto> remoteFilters = correctorRemoteFiltersService.getCorrectedFilters(formDto.getFilters(), mappingFields);
        Map<FieldDto, Collection<Object>> resultRemoteFilters = new HashMap<>();
        if (!remoteFilters.isEmpty()) {
            resultRemoteFilters = remoteFindService.getFilters(remoteFilters, mappingFields);
        }
        if (resultRemoteFilters == null) {
            return Collections.emptyMap();
        }
        Map<FieldDto, FilterDto> localFilters = correctorLocalFiltersService.getCorrectedFilters(formDto.getFilters(), mappingFields);
        return mergeFilters(resultRemoteFilters, localFilters);
    }
}
