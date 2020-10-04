package org.go.together.repository.builder;

import org.go.together.repository.builder.utils.BuilderUtils;
import org.go.together.repository.entities.IdentifiedEntity;

import javax.persistence.ElementCollection;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
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
                .filter(field ->
                        field.getAnnotation(ElementCollection.class) != null ||
                                field.getAnnotation(ManyToMany.class) != null ||
                                field.getAnnotation(OneToMany.class) != null
                )
                .map(Field::getName)
                .forEach(fieldName -> joinTables.put(fieldName, getJoinTableName(fieldName, clazz)));
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
