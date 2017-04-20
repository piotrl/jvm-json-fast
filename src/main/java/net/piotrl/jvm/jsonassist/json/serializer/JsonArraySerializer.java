package net.piotrl.jvm.jsonassist.json.serializer;

import net.piotrl.jvm.jsonassist.json.JsonFactory;
import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

public class JsonArraySerializer {
    public String serializeMethod(String getter) {
        return "" +
                "    java.util.List objects = new java.util.ArrayList(" + getter + ".size());\n" +
                "    Object[] array = " + getter + ".toArray();\n" +
                "    for (int i = 0; i < array.length; i++) {\n" +
                "      objects.add(" + JsonFactory.newJitson("array[i]") +");\n" +
                "    }\n" +
                "   return " + JsonSyntaxBuilder.jsonArrayValue("String.join(\", \", objects)");
    }
}
