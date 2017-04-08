package net.piotrl.jvm.jsonassist.json;

import static net.piotrl.jvm.jsonassist.json.JsonSyntax.*;

public enum JsonSyntaxBuilder {
    ;

    public static String jsonArrayValue(String collectionResult) {
        return "" + ARRAY_START + collectionResult + ARRAY_END;
    }

    public static String jsonNullValue() {
        return JsonSyntax.NULL.toString();
    }

    public static <T> String jsonNullValue(T object) {
        return jsonNullValue();
    }

    public static <T> String jsonStringValue(T result) {
        return "" + STRING + " + " + result.toString() + " + " + STRING;
    }

    public static String jsonObjectValue(String result) {
        return "" + OBJECT_START + result + OBJECT_END;
    }
}
