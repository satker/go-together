package org.go.together.kafka.base;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.kafka.producers.crud.CreateKafkaProducer;
import org.go.together.kafka.producers.crud.DeleteKafkaProducer;
import org.go.together.kafka.producers.crud.ReadKafkaProducer;
import org.go.together.kafka.producers.crud.UpdateKafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.UUID;

import static org.go.together.kafka.enums.ProducerPostfix.*;

public abstract class CrudClient<D extends Dto> extends ValidateClient<D> implements CrudProducer<D> {
    private CreateKafkaProducer<D> createKafkaProducer;
    private DeleteKafkaProducer<D> deleteKafkaProducer;
    private UpdateKafkaProducer<D> updateKafkaProducer;
    private ReadKafkaProducer<D> readKafkaProducer;

    @Autowired
    public void setCreateKafkaProducer(Map<String, CreateKafkaProducer<D>> createKafkaProducers) {
        this.createKafkaProducer = createKafkaProducers.get(getConsumerId() + CREATE.getDescription());
    }

    @Autowired
    public void setDeleteKafkaProducer(Map<String, DeleteKafkaProducer<D>> deleteKafkaProducers) {
        this.deleteKafkaProducer = deleteKafkaProducers.get(getConsumerId() + DELETE.getDescription());
    }

    @Autowired
    public void setUpdateKafkaProducer(Map<String, UpdateKafkaProducer<D>> updateKafkaProducers) {
        this.updateKafkaProducer = updateKafkaProducers.get(getConsumerId() + UPDATE.getDescription());
    }

    @Autowired
    public void setReadKafkaProducer(Map<String, ReadKafkaProducer<D>> readKafkaProducers) {
        this.readKafkaProducer = readKafkaProducers.get(getConsumerId() + READ.getDescription());
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
