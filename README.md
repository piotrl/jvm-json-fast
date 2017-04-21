JITSon aka Jsonassist
=====================

This is a very simple POJO to JSON converter using Javaassit for dynamic converter generator.

Performance in some use cases is 2-10 (!) times higher than GSON.

## Features

- Primitives, primitive collections, nulls
- Simple objects, nested objects, objects with collections
- Recursive objects
- Supports getters / Bean objects
- [Tested](./src/main/test/java/net/piotrl/jvm/jsonassist/JitsonTest.java)

#### Conclusions about Javassist

- Supports only Java 1.4 syntax
    - No Generics
    - No lambda expressions

## How to run

`mvn compile exec:java` - do nothing actually

`mvn compile test`

## Generated code

Jitson generates conversion methods for each class, some examples:

### Simple object

```java
    public String toJson(net.piotrl.jvm.jsonassist.mock.PlainPrimitives o) {
        return "{" + "number: "+o.getNumber()+", "+"longNumber: "+o.getLongNumber()+", "+"string: "+"\"" + o.getString() + "\""+", "+"active: "+o.isActive() + "}"; 
    }
```

### Nested object example

```java
    public String toJson(net.piotrl.jvm.jsonassist.mock.RecursiveObject o) {
        return "{" + "id: "+o.getId()+", "+"child: "+(o.getChild() != null? new net.piotrl.jvm.jsonassist.generation.Jitson().toJson(o.getChild()): null) + "}"; 
    }
```

### Collection example

```java
    public String toJson(java.util.Collection o) {
        java.util.List objects = new java.util.ArrayList(o.size());
        Object[] array = o.toArray();
        for (int i = 0; i < array.length; i++) {
          objects.add(new net.piotrl.jvm.jsonassist.generation.Jitson().toJson(array[i]));
        }
       return "[" + String.join(", ", objects) + "]"; 
    }
```