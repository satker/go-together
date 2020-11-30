package org.go.together.kafka.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;

import java.util.UUID;

public interface CrudProducer<D extends Dto> extends ValidationProducer<D>, FindProducer<D> {
    IdDto create(UUID requestId, D dto);

    IdDto update(UUID requestId, D dto);

    D read(UUID requestId, UUID uuid);

    void delete(UUID requestId, UUID uuid);

    String getConsumerId();
}
