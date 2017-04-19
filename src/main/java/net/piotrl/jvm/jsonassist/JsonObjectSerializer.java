package net.piotrl.jvm.jsonassist;

import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
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
                    return "\"" + property.getDisplayName() + ": \"+" + buildFieldValue(field, value(property));
                })
                .collect(Collectors.joining("+\", \"+"));
    }

    private String value(PropertyDescriptor property) {
        return "o." + property.getReadMethod().getName() + "()";
    }

    private String buildFieldValue(Field field, String getter) {
        return JsonStringifyFactory.factory(field)
                .apply(getter);
    }
}
