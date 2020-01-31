package org.go.together.repository;

import org.go.together.repository.tables.records.InterestRecord;
import org.go.together.repository.tables.records.LanguageRecord;
import org.go.together.repository.tables.records.SystemUserRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.repository.tables.Interest.INTEREST;
import static org.go.together.repository.tables.Language.LANGUAGE;
import static org.go.together.repository.tables.SystemUser.SYSTEM_USER;
import static org.go.together.repository.tables.SystemUserInterest.SYSTEM_USER_INTEREST;
import static org.go.together.repository.tables.SystemUserLanguage.SYSTEM_USER_LANGUAGE;

@Repository
public class UserRepository implements org.go.together.logic.Repository<SystemUserRecord> {
    private DSLContext dsl;

    @Autowired
    public void setDsl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public SystemUserRecord create(SystemUserRecord entity) {
        dsl.executeInsert(entity);
        return null;
    }

    @Override
    public void delete(String uuid) {
        dsl.delete(SYSTEM_USER_INTEREST)
                .where(SYSTEM_USER_INTEREST.USER_ID.eq(uuid))
                .execute();

        dsl.delete(SYSTEM_USER_LANGUAGE)
                .where(SYSTEM_USER_LANGUAGE.USER_ID.eq(uuid))
                .execute();

        dsl.delete(SYSTEM_USER)
                .where(SYSTEM_USER.ID.eq(uuid))
                .execute();
    }

    @Override
    public SystemUserRecord findById(String uuid) {
        return dsl.select(Tables.SYSTEM_USER.fields())
                .from(SYSTEM_USER)
                .where(Tables.SYSTEM_USER.ID.eq(uuid))
                .fetchOne().into(SystemUserRecord.class);
    }

    public Collection<InterestRecord> getInterestsByUser(String uuid) {
        return Stream.of(dsl.select(Tables.INTEREST.fields())
                .from(INTEREST)
                .join(SYSTEM_USER_INTEREST).on(SYSTEM_USER_INTEREST.INTEREST_ID.eq(INTEREST.ID))
                .join(SYSTEM_USER).on(SYSTEM_USER_INTEREST.USER_ID.eq(SYSTEM_USER.ID))
                .where(Tables.SYSTEM_USER.ID.eq(uuid))
                .fetchArray())
                .map(record -> record.into(InterestRecord.class))
                .collect(Collectors.toSet());
    }

    public Collection<LanguageRecord> getLanguagesByUser(String uuid) {
        return Stream.of(dsl.select(LANGUAGE.fields())
                .from(LANGUAGE)
                .join(SYSTEM_USER_LANGUAGE).on(SYSTEM_USER_LANGUAGE.LANGUAGE_ID.eq(LANGUAGE.ID))
                .join(SYSTEM_USER).on(SYSTEM_USER_LANGUAGE.USER_ID.eq(SYSTEM_USER.ID))
                .where(Tables.SYSTEM_USER.ID.eq(uuid))
                .fetchArray())
                .map(record -> record.into(LanguageRecord.class))
                .collect(Collectors.toSet());
    }
}
