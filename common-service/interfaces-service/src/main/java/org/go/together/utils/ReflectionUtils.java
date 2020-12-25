package org.go.together.utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

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
                return getClazzByType(parameterNumber, genericInterface);
            }

            clazz = clazz.getSuperclass();
        } while (clazz != null);

        throw new IllegalArgumentException("Class " + clazz + " doesn't have a generic type.");
    }

    public static Class<?> getClazzByType(int parameterNumber, Type genericInterface) {
        if (genericInterface instanceof ParameterizedType) {
            Type actualTypeArgument = ((ParameterizedType) genericInterface).getActualTypeArguments()[parameterNumber];
            if (actualTypeArgument instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) actualTypeArgument).getRawType();
            }
            if (actualTypeArgument instanceof TypeVariable) {
                return (Class<?>) ((TypeVariable) actualTypeArgument).getBounds()[0];
            }
            return (Class<?>) actualTypeArgument;
        }
        return (Class<?>) genericInterface;
    }

    public static Class<?> getClazz(Type genericInterface) {
        if (genericInterface instanceof Class<?>) {
            return (Class<?>) genericInterface;
        }
        Type actualTypeArgument = ((ParameterizedType) genericInterface).getRawType();
        return (Class<?>) actualTypeArgument;
    }
}
