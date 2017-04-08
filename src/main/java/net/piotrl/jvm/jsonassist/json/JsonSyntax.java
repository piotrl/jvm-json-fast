package net.piotrl.jvm.jsonassist.json;

public enum JsonSyntax {
    NULL("null"),
    STRING("\"\\\"\""), // "\""
    ARRAY_START("["),
    ARRAY_END("]"),
    OBJECT_START("{"),
    OBJECT_END("}");

    private String syntaxElement;

    JsonSyntax(String syntaxElement) {
        this.syntaxElement = syntaxElement;
    }

    public String toString() {
        return syntaxElement;
    }
}
