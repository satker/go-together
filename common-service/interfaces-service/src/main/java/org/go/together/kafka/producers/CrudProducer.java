package org.go.together.kafka.producers;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;

import java.util.UUID;

public interface CrudProducer<D extends Dto> extends ValidationProducer<D>, FindProducer<D> {
    IdDto create(D dto);

    IdDto update(D dto);

    D read(UUID uuid);

    void delete(UUID uuid);
}
