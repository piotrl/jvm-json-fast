package net.piotrl.jvm.jsonassist;

import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

import java.lang.reflect.Field;
import java.util.function.Function;
import java.util.function.Supplier;

public enum JsonStringifyFactory {
    ;

    public static Function<String, String> factory(Field field) {
        if (BeanFieldUtils.isString(field)) {
            return JsonSyntaxBuilder::jsonStringValue;
        }
        if (BeanFieldUtils.isObject(field)) {

            return getter -> nullSafe(getter, () -> "" +
                    "new net.piotrl.jvm.jsonassist.generation.Jitson()" +
                    ".toJson(" + getter + ")");
        }
//        if (BeanFieldUtils.isCollection(field)) {
//            return object -> nullSafe((Collection) object, new JsonArraySerializer()::serialize);
//        }
        return Object::toString;
    }

    static String nullSafe(String getter, Supplier<String> converter) {
        return "(" + getter + " != null" +
                "? " + converter.get() +
                ": " + JsonSyntaxBuilder.jsonNullValue() +
                ")";
    }
}
