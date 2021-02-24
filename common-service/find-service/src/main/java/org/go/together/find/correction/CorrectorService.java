package org.go.together.find.correction;

import org.go.together.compare.FieldMapper;
import org.go.together.find.dto.node.FilterNodeBuilder;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public interface CorrectorService {
    Collection<FilterNodeBuilder> correct(UUID requestId,
                                          Collection<FilterNodeBuilder> nodeBuilder,
                                          Map<String, FieldMapper> availableFields);
}
