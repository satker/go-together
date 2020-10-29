package org.go.together.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectionUtils {
    public static <E> Class<E> getParametrizedClass(Class clazz, int parameterNumber) {
        do {
            Type genericSuperclass = clazz.getGenericSuperclass();
            boolean isParametrizedType = genericSuperclass instanceof ParameterizedType;
            if (isParametrizedType) {
                return (Class<E>) ((ParameterizedType) genericSuperclass).getActualTypeArguments()[parameterNumber];
            }

            clazz = clazz.getSuperclass();
        } while (clazz != null);

        throw new IllegalArgumentException("Class " + clazz + " doesn't have a generic type.");
    }

    public static Class<?> getParametrizedInterface(Class<?> clazz, int parameterNumber) {
        do {
            Type genericInterface = clazz.getGenericInterfaces()[0];
            boolean isParametrizedType = genericInterface instanceof ParameterizedType;
            if (isParametrizedType) {
                Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[parameterNumber];
                if (actualTypeArgument instanceof ParameterizedType) {
                    return (Class<?>) ((ParameterizedType) actualTypeArgument).getRawType();
                }
                return (Class<?>) actualTypeArgument;
            }

            clazz = clazz.getSuperclass();
        } while (clazz != null);

        throw new IllegalArgumentException("Class " + clazz + " doesn't have a generic type.");
    }
}
