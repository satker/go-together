package org.go.together.find.repository.sql.impl;

import lombok.RequiredArgsConstructor;
import org.go.together.base.CustomRepository;
import org.go.together.find.dto.node.ConditionNode;
import org.go.together.find.dto.node.FilterNode;
import org.go.together.find.dto.node.FilterNodeBuilder;
import org.go.together.find.dto.node.Node;
import org.go.together.find.repository.sql.interfaces.QueryFindOperator;
import org.go.together.find.repository.sql.interfaces.WhereBuilderCreator;
import org.go.together.model.IdentifiedEntity;
import org.go.together.repository.query.WhereBuilder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class FilterNodeSqlBuilder<E extends IdentifiedEntity> implements WhereBuilderCreator<E> {
    private final QueryFindOperator queryFindOperator;

    public WhereBuilder<E> create(Collection<FilterNodeBuilder> builders, CustomRepository<E> repository) {
        WhereBuilder<E> where = repository.createWhere();
        builders.stream()
                .map(node -> getWhereByNode(node, repository))
                .forEach(group -> where.group(group).or());
        where.cutLastOr();
        return where;
    }

    private WhereBuilder<E> getWhereByNode(FilterNodeBuilder filterNodeBuilder, CustomRepository<E> repository) {
        Iterator<Node> iterator = filterNodeBuilder.iterator();
        WhereBuilder<E> result = repository.createWhere();
        while (iterator.hasNext()) {
            Node node = iterator.next();
            if (node instanceof FilterNode) {
                FilterNode filterNode = (FilterNode) node;
                queryFindOperator.enrichQuery(filterNode.getValues().getFilterType(),
                        result, filterNode.getField().getLocalField(), filterNode.getValues().getValue());
            } else {
                ConditionNode conditionNode = (ConditionNode) node;
                switch (conditionNode.getPredicate()) {
                    case OR -> result.or();
                    case AND -> result.and();
                }
            }
        }
        return result;
    }
}
