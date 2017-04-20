package net.piotrl.jvm.jsonassist;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public enum BeanFieldUtils {
    ;

    private static Set<Class<?>> wrapperTypes = getWrapperTypes();

    public static PropertyDescriptor getPropertyDescriptor(Class<?> cls, Field field) {
        try {
            return new PropertyDescriptor(field.getName(), cls);
        } catch (IntrospectionException e) {
            throw new RuntimeException(
                    "Cannot build getter | Field: " + field.getName() + " | Class: " + cls.getName(),
                    e);
        }
    }

    public static boolean isObject(Class clazz) {
        return !isString(clazz) && !isNumber(clazz) && !isCollection(clazz);
    }

    public static boolean isNumber(Class<?> type) {
        return type.isPrimitive() || isWrapperType(type);
    }

    public static boolean isString(Class<?> type) {
        return type.isAssignableFrom(String.class);
    }

    public static boolean isCollection(Class clazz) {
        return Collection.class.isAssignableFrom(clazz);
    }

    public static boolean isWrapperType(Class<?> clazz) {
        return wrapperTypes.contains(clazz);
    }

    private static Set<Class<?>> getWrapperTypes() {
        Set<Class<?>> ret = new HashSet<>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        return ret;
    }
}