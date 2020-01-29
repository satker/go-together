package org.go.together.repository;

import org.jooq.DSLContext;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class LanguageRepository implements org.go.together.logic.Repository<UpdatableRecordImpl> {
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
        /*Record record = dsl.select(LANGUAGE.fields())
                .from(LANGUAGE)
                .where(LANGUAGE.ID.eq(uuid))
                .fetchOne();*/
        return null;
    }
}
