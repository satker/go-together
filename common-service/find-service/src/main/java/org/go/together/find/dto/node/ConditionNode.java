package org.go.together.find.dto.node;

import lombok.Builder;
import lombok.Getter;
import org.go.together.enums.SqlPredicate;

@Builder
@Getter
public class ConditionNode implements Node {
    private final SqlPredicate predicate;
}
