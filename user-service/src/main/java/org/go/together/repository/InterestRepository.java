package org.go.together.repository;

import org.go.together.repository.tables.records.InterestRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;

public class InterestRepository implements org.go.together.logic.Repository<InterestRecord> {
    private DSLContext dsl;

    @Autowired
    public void setDsl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public InterestRecord create(InterestRecord entity) {
        return null;
    }

    @Override
    public void delete(String uuid) {

    }

    @Override
    public InterestRecord findById(String uuid) {
        return null;
    }
}
