package net.piotrl.jvm.jsonassist;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public enum BeanFieldUtils {
    ;

    private static Set<Class<?>> wrapperTypes = getWrapperTypes();

    public static Object getValue(Field field, Object instance) throws IntrospectionException, InvocationTargetException, IllegalAccessException {
        Method method = buildGetter(field);
        if (method == null) {
            throw new RuntimeException("Getter not found | field: " + field.getName());
        }
        return method.invoke(instance);
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> cls, Field field) {
        try {
            return new PropertyDescriptor(field.getName(), cls);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    // Source: http://stackoverflow.com/a/2638662/2757140
    public static Method buildGetter(Field field) throws IntrospectionException {
        Class<?> type = field.getDeclaringClass();
        return new PropertyDescriptor(field.getName(), type).getReadMethod();
    }

    /**
     * @return setter analogical to a method {@code buildGetter}
     */
    public static Method buildSetter(Field field) throws IntrospectionException {
        Class<?> type = field.getDeclaringClass();
        return new PropertyDescriptor(field.getName(), type).getWriteMethod();
    }

    public static boolean isObject(Field field) {
        return !isString(field) && !isNumber(field) && !isCollection(field);
    }

    public static boolean isNumber(Field field) {
        return isNumber(field.getType());
    }

    public static boolean isNumber(Class<?> type) {
        return type.isPrimitive() || isWrapperType(type);
    }

    public static boolean isString(Field field) {
        Class<?> type = field.getType();
        return type.isAssignableFrom(String.class);
    }

    public static boolean isCollection(Field field) {
        return Collection.class.isAssignableFrom(field.getType());
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