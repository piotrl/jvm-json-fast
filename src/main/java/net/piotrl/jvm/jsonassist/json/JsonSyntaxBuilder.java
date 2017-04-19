package net.piotrl.jvm.jsonassist.json;

import static net.piotrl.jvm.jsonassist.json.JsonSyntax.*;

public enum JsonSyntaxBuilder {
    ;

    public static String jsonArrayValue(String collectionResult) {
        return "" + ARRAY_START + " + " + collectionResult + " + " + ARRAY_END;
    }

    public static String jsonNullValue() {
        return JsonSyntax.NULL.toString();
    }

    public static String jsonStringValue(String getter) {
        return "" + STRING + " + " + getter + " + " + STRING;
    }

    public static String jsonObjectValue(String result) {
        return "" + OBJECT_START + " + " + result + " + " + OBJECT_END;
    }
}
