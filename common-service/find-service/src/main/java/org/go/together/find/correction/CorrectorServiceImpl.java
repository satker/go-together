package org.go.together.find.correction;

import org.apache.commons.lang3.StringUtils;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FilterValueDto;
import org.go.together.find.correction.path.PathCorrector;
import org.go.together.find.correction.path.dto.Path;
import org.go.together.find.correction.values.ValuesCorrector;
import org.go.together.find.dto.Field;
import org.go.together.find.dto.node.FilterNode;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.go.together.find.dto.node.Node;
import org.go.together.find.finders.Finder;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class CorrectorServiceImpl implements CorrectorService {
    private final ValuesCorrector valuesCorrector;
    private final PathCorrector pathCorrector;
    private final Finder remoteFindService;

    public CorrectorServiceImpl(ValuesCorrector valuesCorrector,
                                PathCorrector pathCorrector, Finder remoteFindService) {
        this.valuesCorrector = valuesCorrector;
        this.pathCorrector = pathCorrector;
        this.remoteFindService = remoteFindService;
    }

    @Override
    public Collection<FilterNodeBuilder> correct(UUID requestId,
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
        Path path = pathCorrector.correct(filterNode.getField(), fieldMappers);
        Field field = new Field(path.getField().getLocalField(), filterNode.getField().getRemoteField());
        filterNode.setField(field);
        Collection<Object> remoteFilters = remoteFindService.getFilters(requestId, filterNode, path.getLastFieldMapper());
        return enrichRemoteFilter(filterNode, remoteFilters);
    }

    private void enrichLocalField(FilterNode filterNode, Map<String, FieldMapper> fieldMappers) {
        Path path = pathCorrector.correct(filterNode.getField(), fieldMappers);
        FilterValueDto values = filterNode.getValues();
        FilterValueDto correctedValuesForSearch = valuesCorrector.correct(path, values);
        filterNode.setValues(correctedValuesForSearch);
        filterNode.setField(path.getField());
    }

    private FilterNode enrichRemoteFilter(FilterNode filterNode, Collection<Object> remoteResult) {
        FilterValueDto filterValueDto = new FilterValueDto();
        filterValueDto.setValue(remoteResult);
        filterValueDto.setFilterType(filterNode.getValues().getFilterType());
        filterNode.setValues(filterValueDto);
        return filterNode;
    }
}
