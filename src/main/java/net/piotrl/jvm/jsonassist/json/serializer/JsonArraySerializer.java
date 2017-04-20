package net.piotrl.jvm.jsonassist.json.serializer;

import net.piotrl.jvm.jsonassist.json.JsonFactory;
import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

public class JsonArraySerializer {
    public String serializeMethod(String getter) {
        return "" +
                "   List<String> objects = new java.util.ArrayList(" + getter + ".size());\n" +
                "   for (Object object : " + getter + ") {\n" +
                "       objects.add(" + JsonFactory.newJitson("object") + ");\n" +
                "   }\n" +
                "   return " + JsonSyntaxBuilder.jsonArrayValue("String.join(\",\", objects)");
    }
}
