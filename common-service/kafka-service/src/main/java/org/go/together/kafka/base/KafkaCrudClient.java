package org.go.together.kafka.base;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.producers.CommonCrudProducer;
import org.go.together.kafka.producers.crud.CreateKafkaProducer;
import org.go.together.kafka.producers.crud.DeleteKafkaProducer;
import org.go.together.kafka.producers.crud.ReadKafkaProducer;
import org.go.together.kafka.producers.crud.UpdateKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public abstract class KafkaCrudClient<D extends Dto> implements CommonCrudProducer<D> {
    private CreateKafkaProducer<D> createKafkaProducer;
    private DeleteKafkaProducer<D> deleteKafkaProducer;
    private UpdateKafkaProducer<D> updateKafkaProducer;
    private ReadKafkaProducer<D> readKafkaProducer;

    @Autowired
    public void setCreateKafkaProducer(CreateKafkaProducer<D> createKafkaProducer) {
        this.createKafkaProducer = createKafkaProducer;
    }

    @Autowired
    public void setDeleteKafkaProducer(DeleteKafkaProducer<D> deleteKafkaProducer) {
        this.deleteKafkaProducer = deleteKafkaProducer;
    }

    @Autowired
    public void setUpdateKafkaProducer(UpdateKafkaProducer<D> updateKafkaProducer) {
        this.updateKafkaProducer = updateKafkaProducer;
    }

    @Autowired
    public void setReadKafkaProducer(ReadKafkaProducer<D> readKafkaProducer) {
        this.readKafkaProducer = readKafkaProducer;
    }

    @Override
    public IdDto create(UUID requestId, D dto) {
        return createKafkaProducer.create(requestId, dto);
    }

    @Override
    public IdDto update(UUID requestId, D dto) {
        return updateKafkaProducer.update(requestId, dto);
    }

    @Override
    public D read(UUID requestId, UUID uuid) {
        return readKafkaProducer.read(requestId, uuid);
    }

    @Override
    public void delete(UUID requestId, UUID uuid) {
        deleteKafkaProducer.delete(requestId, uuid);
    }
}
