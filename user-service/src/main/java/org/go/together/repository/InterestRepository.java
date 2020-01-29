package org.go.together.repository;

import org.jooq.DSLContext;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class InterestRepository implements org.go.together.logic.Repository<UpdatableRecordImpl> {
    private DSLContext dsl;

    @Autowired
    public void setDsl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public UpdatableRecordImpl create(UpdatableRecordImpl entity) {
        return null;
    }

    @Override
    public void delete(UUID uuid) {

    }

    @Override
    public UpdatableRecordImpl findById(UUID uuid) {
        return null;
    }
}
