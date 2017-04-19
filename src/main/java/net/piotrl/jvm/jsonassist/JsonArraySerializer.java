package net.piotrl.jvm.jsonassist;

import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

public class JsonArraySerializer {
    public String serialize(String getter) {
        return JsonSyntaxBuilder.jsonArrayValue(fillElements(getter));
    }

    private String fillElements(String getter) {
        return getter + ".stream()" +
                ".map(el -> " + this.parseField("el") + ")" +
                ".collect(java.util.stream.Collectors.joining(\", \"))";
    }

    private String parseField(String getter) {
        return new JsonObjectSerializer().serialize(getter);
    }
}
