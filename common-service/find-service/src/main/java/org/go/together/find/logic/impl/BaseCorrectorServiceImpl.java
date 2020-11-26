package org.go.together.find.logic.impl;

import org.go.together.compare.FieldMapper;
import org.go.together.dto.form.FilterDto;
import org.go.together.dto.form.FormDto;
import org.go.together.find.correction.CorrectorService;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.finders.Finder;
import org.go.together.find.logic.interfaces.BaseCorrectorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;

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
    public Map<FieldDto, FilterDto> getCorrectedFilters(UUID requestId, FormDto formDto, Map<String, FieldMapper> mappingFields) {
        Map<FieldDto, FilterDto> remoteFilters = correctorRemoteFiltersService.getCorrectedFilters(formDto.getFilters(), mappingFields);
        Map<FieldDto, Collection<Object>> resultRemoteFilters = new HashMap<>();
        if (!remoteFilters.isEmpty()) {
            resultRemoteFilters = remoteFindService.getFilters(requestId, remoteFilters, mappingFields);
        }
        if (resultRemoteFilters == null) {
            return Collections.emptyMap();
        }
        Map<FieldDto, FilterDto> localFilters = correctorLocalFiltersService.getCorrectedFilters(formDto.getFilters(), mappingFields);
        return mergeFilters(resultRemoteFilters, localFilters);
    }
}
