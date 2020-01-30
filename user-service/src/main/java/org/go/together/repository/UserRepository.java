package org.go.together.repository;

import org.go.together.repository.tables.records.AppuserRecord;
import org.go.together.repository.tables.records.InterestRecord;
import org.go.together.repository.tables.records.LanguageRecord;
import org.jooq.DSLContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.go.together.repository.tables.Appuser.APPUSER;
import static org.go.together.repository.tables.AppuserInterest.APPUSER_INTEREST;
import static org.go.together.repository.tables.AppuserLanguage.APPUSER_LANGUAGE;
import static org.go.together.repository.tables.Interest.INTEREST;
import static org.go.together.repository.tables.Language.LANGUAGE;

@Repository
public class UserRepository implements org.go.together.logic.Repository<AppuserRecord> {
    private DSLContext dsl;

    @Autowired
    public void setDsl(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Override
    public AppuserRecord create(AppuserRecord entity) {
        dsl.executeInsert(entity);
        return null;
    }

    @Override
    public void delete(String uuid) {
        dsl.delete(APPUSER_INTEREST)
                .where(APPUSER_INTEREST.APPUSER_ID.eq(uuid))
                .execute();

        dsl.delete(APPUSER_LANGUAGE)
                .where(APPUSER_LANGUAGE.APPUSER_ID.eq(uuid))
                .execute();

        dsl.delete(APPUSER)
                .where(APPUSER.ID.eq(uuid))
                .execute();
    }

    @Override
    public AppuserRecord findById(String uuid) {
        return dsl.select(Tables.APPUSER.fields())
                .from(APPUSER)
                .where(Tables.APPUSER.ID.eq(uuid))
                .fetchOne().into(AppuserRecord.class);
    }

    public Collection<InterestRecord> getInterestsByUser(String uuid) {
        return Stream.of(dsl.select(Tables.INTEREST.fields())
                .from(INTEREST)
                .join(APPUSER_INTEREST).on(APPUSER_INTEREST.INTERESTS_ID.eq(INTEREST.ID))
                .join(APPUSER).on(APPUSER_INTEREST.APPUSER_ID.eq(APPUSER.ID))
                .where(Tables.APPUSER.ID.eq(uuid))
                .fetchArray())
                .map(record -> record.into(InterestRecord.class))
                .collect(Collectors.toSet());
    }

    public Collection<LanguageRecord> getLanguagesByUser(String uuid) {
        return Stream.of(dsl.select(LANGUAGE.fields())
                .from(LANGUAGE)
                .join(APPUSER_LANGUAGE).on(APPUSER_LANGUAGE.LANGUAGES_ID.eq(LANGUAGE.ID))
                .join(APPUSER).on(APPUSER_LANGUAGE.APPUSER_ID.eq(APPUSER.ID))
                .where(Tables.APPUSER.ID.eq(uuid))
                .fetchArray())
                .map(record -> record.into(LanguageRecord.class))
                .collect(Collectors.toSet());
    }
}
