package net.piotrl.jvm.jsonassist;

import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.stream.Collectors;


public class JsonObjectSerializer {

    public <T> String serialize(Class<T> value) {
        return JsonSyntaxBuilder.jsonObjectValue(fillFields(value));
    }

    private String fillFields(Class<?> cls) {
        return Arrays.stream(cls.getDeclaredFields())
                .map(field -> {
                    PropertyDescriptor property = BeanFieldUtils.getPropertyDescriptor(cls, field);
                    return "\"" + property.getDisplayName() + "\\: \"+" + buildFieldValue(field, value(property));
                })
                .collect(Collectors.joining("+\", \"+"));
    }

    private String value(PropertyDescriptor property) {
        return "o." + property.getReadMethod().getName() + "()";
    }

    private String stringFieldValue(Field field, Object src) {
        Object result = getFieldValue(field, src);
        return buildFieldValue(field, result);
    }

    private Object getFieldValue(Field field, Object src) {
        try {
            return BeanFieldUtils.getValue(field, src);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Illegal access | field: " + field.getName(), e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Can't get result of method | field: " + field.getName(), e);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Can't build getter | field: " + field.getName(), e);
        }
    }

    private String buildFieldValue(Field field, Object result) {
        if (result == null) {
            return JsonSyntaxBuilder.jsonNullValue();
        }

        return JsonStringifyFactory.factory(field)
                .apply(result);
    }
}
