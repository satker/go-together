package org.go.together.repository.builder;

import org.apache.commons.lang3.StringUtils;
import org.go.together.repository.builder.utils.BuilderUtils;
import org.go.together.repository.entities.IdentifiedEntity;

import javax.persistence.ElementCollection;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static org.go.together.repository.builder.utils.BuilderUtils.getEntityField;

public class JoinBuilder<E extends IdentifiedEntity> {
    private final Map<String, String> joinTables = new HashMap<>();
    private final Class<E> clazz;

    public JoinBuilder(Class<E> clazz) {
        this.clazz = clazz;
        Arrays.stream(clazz.getDeclaredFields())
                .filter(field ->
                        field.getAnnotation(ElementCollection.class) != null ||
                                field.getAnnotation(ManyToMany.class) != null ||
                                field.getAnnotation(ManyToOne.class) != null ||
                                field.getAnnotation(OneToMany.class) != null
                )
                .map(Field::getName)
                .forEach(fieldName -> joinTables.put(fieldName, getJoinTableName(fieldName, clazz)));
    }

    private String getJoinTableName(String fieldName, Class<E> clazz) {
        return BuilderUtils.getEntityLink(clazz) + "_" + fieldName;
    }

    public Optional<Map.Entry<String, String>> getJoin(String field) {
        return joinTables.entrySet().stream()
                .filter(joinName -> field.startsWith(joinName.getKey()))
                .findFirst();
    }

    public String createLeftJoin(Map.Entry<String, String> joinTableName) {
        return " left join " + BuilderUtils.getEntityField(joinTableName.getKey(), clazz) + StringUtils.SPACE + joinTableName.getValue();
    }

    public String getFieldWithJoin(String field, Consumer<Map.Entry<String, String>> enrichFunction) {
        return getJoin(field).map(entry -> {
            enrichFunction.accept(entry);
            return field.replaceFirst(entry.getKey(), entry.getValue());
        }).orElse(getEntityField(field, clazz));
    }
}
