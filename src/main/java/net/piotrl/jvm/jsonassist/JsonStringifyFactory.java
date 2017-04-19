package net.piotrl.jvm.jsonassist;

import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

import java.lang.reflect.Field;
import java.util.function.Function;

public enum JsonStringifyFactory {
    ;

    public static Function<String, String> factory(Class clazz) {
        if (BeanFieldUtils.isString(clazz)) {
            return JsonSyntaxBuilder::jsonStringValue;
        }
        if (BeanFieldUtils.isObject(clazz)) {
            return getter -> nullSafe(getter, __ -> new JsonObjectSerializer().serialize(clazz, getter));
        }
        if (BeanFieldUtils.isCollection(clazz)) {
            return getter -> nullSafe(getter, new JsonArraySerializer()::serialize);
        }
        return Object::toString;
    }

    public static Function<String, String> factory(Field field) {
        return JsonStringifyFactory.factory(field.getType());
    }

    static String nullSafe(String getter, Function<String, String> converter) {
        return "(" + getter + " != null" +
                "? " + converter.apply(getter) +
                ": " + JsonSyntaxBuilder.jsonNullValue() +
                ")";
    }
}
