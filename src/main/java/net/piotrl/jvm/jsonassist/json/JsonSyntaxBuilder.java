package net.piotrl.jvm.jsonassist.json;


public enum JsonSyntaxBuilder {
    ;
    private static final String NULL = "null";
    private static final String STRING = "\"\\\"\"";
    private static final String ARRAY_START = "\"[\"";
    private static final String ARRAY_END = "\"]\"";
    private static final String OBJECT_START = "\"{\"";
    private static final String OBJECT_END = "\"}\"";

    public static String jsonArrayValue(String collectionResult) {
        return "" + ARRAY_START + " + " + collectionResult + " + " + ARRAY_END;
    }

    public static String jsonNullValue() {
        return NULL;
    }

    public static String jsonStringValue(String getter) {
        return "" + STRING + " + " + getter + " + " + STRING;
    }

    public static String jsonObjectValue(String result) {
        return "" + OBJECT_START + " + " + result + " + " + OBJECT_END;
    }
}
