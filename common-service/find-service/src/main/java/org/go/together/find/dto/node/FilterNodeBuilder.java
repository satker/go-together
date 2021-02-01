package org.go.together.find.dto.node;

import org.go.together.dto.FilterValueDto;
import org.go.together.enums.SqlPredicate;
import org.go.together.find.dto.Field;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FilterNodeBuilder {
    private final List<Node> nodes;

    public FilterNodeBuilder() {
        this.nodes = new LinkedList<>();
    }

    public boolean condition(SqlPredicate predicate) {
        ConditionNode conditionNode = ConditionNode.builder().predicate(predicate).build();
        return nodes.add(conditionNode);
    }

    public boolean filter(String fieldStr, FilterValueDto values) {
        FilterNode node = new FilterNode();
        node.setField(new Field(fieldStr));
        node.setValues(values);
        return nodes.add(node);
    }

    public Iterator<Node> iterator() {
        return nodes.iterator();
    }
}
