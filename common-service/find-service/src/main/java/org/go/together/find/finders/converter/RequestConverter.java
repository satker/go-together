package org.go.together.find.finders.converter;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.find.dto.node.FilterNode;
import org.go.together.kafka.producers.FindProducer;

public interface RequestConverter {
    Pair<FindProducer<?>, FormDto> convert(FilterNode entry,
                                           FieldMapper fieldMapper);
}
