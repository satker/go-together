package org.go.together.find.correction;

import org.apache.commons.lang3.StringUtils;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FilterValueDto;
import org.go.together.find.correction.field.dto.CorrectedFieldDto;
import org.go.together.find.correction.fieldpath.FieldPathCorrector;
import org.go.together.find.correction.values.ValuesCorrector;
import org.go.together.find.dto.FieldDto;
import org.go.together.find.dto.node.FilterNode;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.go.together.find.dto.node.Node;
import org.go.together.find.finders.Finder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CorrectorServiceImpl implements CorrectorService {
    private final FieldPathCorrector fieldPathCorrector;
    private final ValuesCorrector valuesCorrector;
    private final Finder remoteFindService;

    public CorrectorServiceImpl(FieldPathCorrector fieldPathCorrector,
                                ValuesCorrector valuesCorrector,
                                Finder remoteFindService) {
        this.fieldPathCorrector = fieldPathCorrector;
        this.valuesCorrector = valuesCorrector;
        this.remoteFindService = remoteFindService;
    }

    @Override
    public Collection<FilterNodeBuilder> getCorrectedFilters(UUID requestId,
                                                 Collection<FilterNodeBuilder> nodeBuilder,
                                                        Map<String, FieldMapper> availableFields) {
        return nodeBuilder.stream()
                .map(node -> getFilterNodeBuilder(requestId, node, availableFields))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private FilterNodeBuilder getFilterNodeBuilder(UUID requestId, FilterNodeBuilder nodeBuilder, Map<String, FieldMapper> availableFields) {
        Iterator<Node> iterator = nodeBuilder.iterator();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node instanceof FilterNode) {
                FilterNode filterNode = (FilterNode) node;
                if (isRemoteNode(filterNode)) {
                    FilterNode remoteField = enrichRemoteField(requestId, filterNode, availableFields);
                    if (((Collection) remoteField.getValues().getValue()).isEmpty()) {
                        return null;
                    }
                } else {
                    enrichLocalField(filterNode, availableFields);
                }
            }
        }
        return nodeBuilder;
    }

    private boolean isRemoteNode(FilterNode filterNode) {
        return StringUtils.isNotBlank(filterNode.getField().getRemoteField());
    }

    private FilterNode enrichRemoteField(UUID requestId, FilterNode filterNode, Map<String, FieldMapper> fieldMappers) {
        CorrectedFieldDto localFieldForSearch = fieldPathCorrector.getCorrectedFieldDto(filterNode.getField(), fieldMappers);
        FieldDto fieldDto = localFieldForSearch.getFieldDto();
        filterNode.setField(fieldDto);
        Collection<Object> remoteFilters = remoteFindService.getFilters(requestId, filterNode, localFieldForSearch.getFieldMapper());
        return enrichRemoteFilter(filterNode, remoteFilters);
    }

    private void enrichLocalField(FilterNode filterNode, Map<String, FieldMapper> fieldMappers) {
        CorrectedFieldDto localFieldForSearch = fieldPathCorrector.getCorrectedFieldDto(filterNode.getField(), fieldMappers);
        FilterValueDto values = filterNode.getValues();
        FilterValueDto correctedValuesForSearch = valuesCorrector.correct(localFieldForSearch, values);
        filterNode.setValues(correctedValuesForSearch);
        filterNode.setField(localFieldForSearch.getFieldDto());
    }

    private FilterNode enrichRemoteFilter(FilterNode filterNode, Collection<Object> remoteResult) {
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setValue(remoteResult);
        filterValueDto.setFilterType(filterNode.getValues().getFilterType());
        filterNode.setValues(filterValueDto);
        return filterNode;
    }
}
