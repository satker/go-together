package org.go.together.repository;

import org.go.together.CustomRepository;
import org.go.together.model.GroupPhoto;
import org.go.together.sql.SqlOperator;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Repository
public class GroupPhotoRepository extends CustomRepository<GroupPhoto> {
    @Transactional
    public Optional<GroupPhoto> findByEventId(UUID eventPhotoId) {
        return createQuery().where(createWhere().condition("groupId", SqlOperator.EQUAL, eventPhotoId)).fetchOne();
    }
}
