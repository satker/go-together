package org.go.together.repository.builders;

import org.go.together.model.IdentifiedEntity;

import java.util.Map;

public interface Where<E extends IdentifiedEntity> extends Query<E> {
    StringBuilder getWhereQuery();

    Map<String, String> getJoin();
}
