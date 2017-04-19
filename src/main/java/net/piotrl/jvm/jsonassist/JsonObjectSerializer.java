package net.piotrl.jvm.jsonassist;

import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;


public class JsonObjectSerializer {
    public String serialize(Class clazz, String getter) {
        return JsonSyntaxBuilder.jsonObjectValue(fillFields(clazz, getter));
    }

    public String serialize(String getter) {
        return "" +
                "new net.piotrl.jvm.jsonassist.generation.Jitson()" +
                ".toJson(" + getter + ")";
    }

    private String fillFields(Class clazz, String getter) {
        return Arrays.stream(clazz.getDeclaredFields())
                .map(field -> {
                    PropertyDescriptor property = BeanFieldUtils.getPropertyDescriptor(clazz, field);
                    return "\"" + property.getDisplayName() + ": \"+" + buildFieldValue(field, value(getter, property));
                })
                .collect(Collectors.joining("+\", \"+"));
    }

    private String value(String getter, PropertyDescriptor property) {
        return getter + "." + property.getReadMethod().getName() + "()";
    }

    private String buildFieldValue(Field field, String getter) {
        return JsonStringifyFactory.factory(field)
                .apply(getter);
    }
}
