package org.go.together.find.finders;

import org.apache.commons.lang3.tuple.Pair;
import org.go.together.compare.FieldMapper;
import org.go.together.dto.FormDto;
import org.go.together.find.dto.node.FilterNode;
import org.go.together.find.finders.converter.RequestConverter;
import org.go.together.find.finders.request.Sender;
import org.go.together.kafka.producers.FindProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.UUID;

@Component
public class RemoteFinder implements Finder {
    private RequestConverter requestConverter;
    private Sender sender;

    @Autowired
    public void setSender(Sender sender) {
        this.sender = sender;
    }

    @Autowired
    public void setRequestConverter(RequestConverter requestConverter) {
        this.requestConverter = requestConverter;
    }

    public Collection<Object> getFilters(UUID requestId,
                                         FilterNode filterNode,
                                         FieldMapper fieldMapper) {
        Pair<FindProducer<?>, FormDto> producerFormDtoPair = requestConverter.convert(filterNode, fieldMapper);
        return sender.send(requestId, producerFormDtoPair.getKey(), producerFormDtoPair.getValue());
    }
}
