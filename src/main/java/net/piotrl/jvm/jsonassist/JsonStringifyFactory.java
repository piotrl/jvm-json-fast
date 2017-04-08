package net.piotrl.jvm.jsonassist;

import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

import java.lang.reflect.Field;
import java.util.function.Function;

public enum JsonStringifyFactory {
    ;

    public static Function<Object, String> factory(Field field) {

        if (BeanFieldUtils.isString(field)) {
            return JsonSyntaxBuilder::jsonStringValue;
        }
        if (BeanFieldUtils.isObject(field)) {
            return object -> nullSafe(object, new JsonObjectSerializer()::serialize);
        }
//        if (BeanFieldUtils.isCollection(field)) {
//            return object -> nullSafe((Collection) object, new JsonArraySerializer()::serialize);
//        }
        return Object::toString;
    }

    static <T> Function<T, String> factory(T objectToParse) {
        if (objectToParse == null) {
            return JsonSyntaxBuilder::jsonNullValue;
        }
        Class<?> type = objectToParse.getClass();
        if (BeanFieldUtils.isNumber(type)) {
            return Object::toString;
        }
        if (objectToParse instanceof String) {
            return JsonSyntaxBuilder::jsonStringValue;
        }
//        if (objectToParse instanceof Collection) {
//            return value -> new JsonArraySerializer().serialize((Collection) value);
//        }
        return value -> new JsonObjectSerializer().serialize(value);
    }

    static <T> String nullSafe(T value, Function<T, String> fallback) {
        if (value == null) {
            return JsonSyntaxBuilder.jsonNullValue();
        }
        return fallback.apply(value);
    }
}
