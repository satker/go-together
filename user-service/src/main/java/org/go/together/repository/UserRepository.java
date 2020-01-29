package org.go.together.repository;

import org.jooq.DSLContext;
import org.jooq.impl.UpdatableRecordImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

@Repository
public class UserRepository implements org.go.together.logic.Repository<UpdatableRecordImpl> {
    private DSLContext dsl;

    @Autowired
    public void setDsl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public UpdatableRecordImpl create(UpdatableRecordImpl entity) {
        //dsl.executeInsert(entity);
        return null;
    }

    @Override
    public void delete(UUID uuid) {
        /*dsl.delete(USER_INTEREST)
                .where(USER_INTEREST.APP_USER_ID.eq(uuid))
                .execute();

        dsl.delete(USER_LANGUAGE)
                .where(USER_LANGUAGE.APP_USER_ID.eq(uuid))
                .execute();

        dsl.delete(APP_USER)
                .where(APP_USER.ID.eq(uuid))
                .execute();*/
    }

    @Override
    public UpdatableRecordImpl findById(UUID uuid) {
        return /*dsl.select(Tables.APP_USER.fields())
                .from(APP_USER)
                .where(Tables.APP_USER.ID.eq(UUID.fromString("a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")))
                .fetchOne().into(AppUserRecord.class)*/null;
    }

    public Collection<UpdatableRecordImpl> getInterestsByUser(UUID uuid) {
        return /*Stream.of(dsl.select(Tables.INTEREST.fields())
                .from(INTEREST)
                .join(USER_INTEREST).on(USER_INTEREST.INTEREST_ID.eq(INTEREST.ID))
                .join(APP_USER).on(USER_INTEREST.APP_USER_ID.eq(APP_USER.ID))
                .where(Tables.APP_USER.ID.eq(uuid))
                .fetchArray())
                .map(record -> record.into(InterestRecord.class))
                .collect(Collectors.toSet())*/null;
    }

    public Collection<UpdatableRecordImpl> getLanguagesByUser(UUID uuid) {
        return /*Stream.of(dsl.select(LANGUAGE.fields())
                .from(LANGUAGE)
                .join(USER_LANGUAGE).on(USER_LANGUAGE.LANGUAGE_ID.eq(LANGUAGE.ID))
                .join(APP_USER).on(USER_LANGUAGE.APP_USER_ID.eq(APP_USER.ID))
                .where(Tables.APP_USER.ID.eq(uuid))
                .fetchArray())
                .map(record -> record.into(LanguageRecord.class))
                .collect(Collectors.toSet())*/null;
    }
}
