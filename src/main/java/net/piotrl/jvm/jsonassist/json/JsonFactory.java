package net.piotrl.jvm.jsonassist.json;

import net.piotrl.jvm.jsonassist.BeanFieldUtils;
import net.piotrl.jvm.jsonassist.json.serializer.JsonArraySerializer;
import net.piotrl.jvm.jsonassist.json.serializer.JsonObjectSerializer;

import java.lang.reflect.Field;
import java.util.function.Function;

public enum JsonFactory {
    ;

    // First level objects to build whole method
    public static Function<String, String> factory(Class clazz) {
        if (BeanFieldUtils.isString(clazz)) {
            return getter -> "return " + JsonSyntaxBuilder.jsonStringValue(getter);
        }
        if (BeanFieldUtils.isObject(clazz)) {
            return getter -> "return " + new JsonObjectSerializer().serialize(clazz, getter);
        }
        if (BeanFieldUtils.isCollection(clazz)) {
            return new JsonArraySerializer()::serializeMethod;
        }
        return getter -> "return Objects.toString(" + getter + ")";
    }

    // Second and more level to build JSON partial objects (with nullChecks)
    public static Function<String, String> factory(Field field) {
        Class clazz = field.getType();
        if (BeanFieldUtils.isString(clazz)) {
            return JsonSyntaxBuilder::jsonStringValue;
        }
        if (BeanFieldUtils.isObject(clazz)) {
            return getter -> nullSafe(getter, __ -> new JsonObjectSerializer().serialize(clazz, getter));
        }
        if (BeanFieldUtils.isCollection(clazz)) {
            return getter -> nullSafe(getter, JsonFactory::newJitson);
        }

        return Object::toString;
    }

    public static String newJitson(String getter) {
        return "new net.piotrl.jvm.jsonassist.generation.Jitson().toJson(" + getter + ")";
    }

    static String nullSafe(String getter, Function<String, String> converter) {
        return "(" + getter + " != null" +
                "? " + converter.apply(getter) +
                ": " + JsonSyntaxBuilder.jsonNullValue() +
                ")";
    }
}
