package org.go.together.repository.builder;

import org.go.together.repository.builder.utils.BuilderUtils;
import org.go.together.repository.entities.IdentifiedEntity;
import org.go.together.repository.interfaces.JoinBuilder;
import org.go.together.repository.interfaces.Query;

import javax.persistence.ElementCollection;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.go.together.repository.builder.utils.BuilderUtils.getEntityField;

public class Join<E extends IdentifiedEntity> implements Query<E> {
    private final Map<String, String> joinTables;
    private final Map<String, String> currentJoinTables;
    private final Class<E> clazz;

    private Join(Map<String, String> joinTables, Class<E> clazz) {
        this.joinTables = joinTables;
        this.currentJoinTables = new HashMap<>();
        this.clazz = clazz;
    }

    public static <E extends IdentifiedEntity> JoinBuilderImpl<E> builder() {
        return new JoinBuilderImpl<>();
    }

    public String getFieldWithJoin(String field) {
        return getJoin(field).map(entry -> {
            currentJoinTables.put(entry.getKey(), entry.getValue());
            return field.replaceFirst(entry.getKey(), entry.getValue());
        }).orElse(getEntityField(field, clazz));
    }

    private Optional<Map.Entry<String, String>> getJoin(String field) {
        return joinTables.entrySet().stream()
                .filter(joinName -> field.startsWith(joinName.getKey()))
                .findFirst();
    }

    public Map<String, String> getJoinTables() {
        return currentJoinTables;
    }

    public static class JoinBuilderImpl<B extends IdentifiedEntity> implements JoinBuilder<B> {
        private Map<String, String> joinTables;
        private Class<B> clazz;

        public JoinBuilderImpl<B> clazz(Class<B> clazz) {
            this.clazz = clazz;
            joinTables = new HashMap<>();
            Arrays.stream(clazz.getDeclaredFields())
                    .filter(field ->
                            field.getAnnotation(ElementCollection.class) != null ||
                                    field.getAnnotation(ManyToMany.class) != null ||
                                    field.getAnnotation(ManyToOne.class) != null ||
                                    field.getAnnotation(OneToMany.class) != null
                    )
                    .map(Field::getName)
                    .forEach(fieldName -> joinTables.put(fieldName, BuilderUtils.getEntityLink(clazz) + "_" + fieldName));
            return this;
        }

        public Join<B> build() {
            return new Join<>(joinTables, clazz);
        }
    }
}
