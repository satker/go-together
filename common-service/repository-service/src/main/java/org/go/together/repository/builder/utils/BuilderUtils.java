package org.go.together.repository.builder.utils;

import org.apache.commons.lang3.StringUtils;
import org.go.together.model.IdentifiedEntity;

import java.util.Map;

public class BuilderUtils {
    public static <E extends IdentifiedEntity> String getEntityLink(Class<E> clazz) {
        char[] chars = clazz.getSimpleName().toCharArray();
        StringBuilder entityLink = new StringBuilder();
        for (char aChar : chars) {
            if (Character.isUpperCase(aChar)) {
                entityLink.append(aChar);
            }
        }
        return entityLink.toString().toLowerCase();
    }

    public static <E extends IdentifiedEntity> String getEntityField(String field, Class<E> clazz) {
        return getEntityLink(clazz) + "." + field;
    }

    public static <E extends IdentifiedEntity> String createLeftJoin(Map.Entry<String, String> joinTableName,
                                                                     Class<E> clazz,
                                                                     boolean isNeedFetch) {
        final String LEFT_JOIN = " left join ";
        String fetchJoin = isNeedFetch ? "fetch " : StringUtils.EMPTY;
        return LEFT_JOIN + fetchJoin + BuilderUtils.getEntityField(joinTableName.getKey(), clazz) +
                StringUtils.SPACE + joinTableName.getValue();
    }
}
