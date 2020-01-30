package org.go.together.repository;

import org.go.together.repository.tables.records.LanguageRecord;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import static org.go.together.repository.tables.Language.LANGUAGE;

@Repository
public class LanguageRepository implements org.go.together.logic.Repository<LanguageRecord> {
    private DSLContext dsl;

    @Autowired
    public void setDsl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public LanguageRecord create(LanguageRecord entity) {
        return null;
    }

    @Override
    public void delete(String uuid) {

    }

    @Override
    public LanguageRecord findById(String uuid) {
        LanguageRecord languageRecord = new LanguageRecord();
        Record record = dsl.select(LANGUAGE.fields())
                .from(LANGUAGE)
                .where(LANGUAGE.ID.eq(uuid))
                .fetchOne();
        return null;
    }
}
