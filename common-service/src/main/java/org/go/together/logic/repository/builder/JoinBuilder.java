package org.go.together.logic.repository.builder;

import org.go.together.interfaces.IdentifiedEntity;
import org.go.together.logic.repository.builder.utils.BuilderUtils;

import javax.persistence.ElementCollection;
import javax.persistence.ManyToMany;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JoinBuilder<E extends IdentifiedEntity> {
    private final Map<String, String> joinTables = new HashMap<>();
    private final Class<E> clazz;

    public JoinBuilder(Class<E> clazz) {
        this.clazz = clazz;
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field1 -> field1.getAnnotation(ElementCollection.class) != null || field1.getAnnotation(ManyToMany.class) != null)
                .map(Field::getName)
                .forEach(fieldName -> {
                    String generatedTableName = getJoinTableName(fieldName, clazz);
                    joinTables.put(fieldName, generatedTableName);
                });
    }

    private String getJoinTableName(String fieldName, Class<E> clazz) {
        return BuilderUtils.getEntityLink(clazz) + "_" + fieldName;
    }

    public Map<String, String> getJoinTables() {
        return joinTables;
    }

    public String createLeftJoin(Map.Entry<String, String> joinTableName) {
        return " left join " + BuilderUtils.getEntityField(joinTableName.getKey(), clazz) + " " + joinTableName.getValue();
    }
}
