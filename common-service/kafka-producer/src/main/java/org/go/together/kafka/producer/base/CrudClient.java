package org.go.together.kafka.producer.base;

import org.go.together.dto.Dto;
import org.go.together.dto.IdDto;
import org.go.together.kafka.producers.CrudProducer;
import org.go.together.kafka.producers.crud.CreateKafkaProducer;
import org.go.together.kafka.producers.crud.DeleteKafkaProducer;
import org.go.together.kafka.producers.crud.ReadKafkaProducer;
import org.go.together.kafka.producers.crud.UpdateKafkaProducer;

import java.util.UUID;

public abstract class CrudClient<D extends Dto> extends ValidateClient<D> implements CrudProducer<D> {
    private CreateKafkaProducer<D> createKafkaProducer;
    private DeleteKafkaProducer<D> deleteKafkaProducer;
    private UpdateKafkaProducer<D> updateKafkaProducer;
    private ReadKafkaProducer<D> readKafkaProducer;

    public void setCreateKafkaProducer(CreateKafkaProducer<D> createKafkaProducer) {
        this.createKafkaProducer = createKafkaProducer;
    }

    public void setDeleteKafkaProducer(DeleteKafkaProducer<D> deleteKafkaProducer) {
        this.deleteKafkaProducer = deleteKafkaProducer;
    }

    public void setUpdateKafkaProducer(UpdateKafkaProducer<D> updateKafkaProducer) {
        this.updateKafkaProducer = updateKafkaProducer;
    }

    public void setReadKafkaProducer(ReadKafkaProducer<D> readKafkaProducer) {
        this.readKafkaProducer = readKafkaProducer;
    }

    @Override
    public IdDto create(D dto) {
        return createKafkaProducer.create(dto);
    }

    @Override
    public IdDto update(D dto) {
        return updateKafkaProducer.update(dto);
    }

    @Override
    public D read(UUID uuid) {
        return readKafkaProducer.read(uuid);
    }

    @Override
    public void delete(UUID uuid) {
        deleteKafkaProducer.delete(uuid);
    }
}
