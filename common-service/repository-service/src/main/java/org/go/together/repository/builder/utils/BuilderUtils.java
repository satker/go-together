package org.go.together.repository.builder.utils;

import org.go.together.interfaces.IdentifiedEntity;

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
}
