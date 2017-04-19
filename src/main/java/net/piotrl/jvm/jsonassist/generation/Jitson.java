package net.piotrl.jvm.jsonassist.generation;

import javassist.*;
import net.piotrl.jvm.jsonassist.JsonObjectSerializer;
import net.piotrl.jvm.jsonassist.JsonStringifyFactory;
import net.piotrl.jvm.jsonassist.json.JsonSyntaxBuilder;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Jitson {

	private ClassPool pool;
	private static Map<Class<?>, JsonConverter> cache = new ConcurrentHashMap<>();

	public Jitson() {
		pool = ClassPool.getDefault();
	}

	/**
	 * Converts given object to JSON. Supports only public fields. No support
	 * for nested conversions (toString is used for value).
	 *
	 * @param o
	 *            Object to be converted.
	 * @return JSON in a String
	 * @throws Exception
	 */
	public String toJson(Object o) {
		if (o == null) {
			return "null";
		}
		Class<?> type = o.getClass();
		JsonConverter a = getJsonConverter(type);
		return a.toJson(o);
	}

	public JsonConverter getJsonConverter(Class<?> type) {
		try {
			if (!cache.containsKey(type)) {
				cache.put(type, getConverter(type));
			}
			return cache.get(type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private JsonConverter getConverter(Class<?> cls)
			throws CannotCompileException, NotFoundException, InstantiationException, IllegalAccessException {

		// new class with a random name, as this name is not needed in any way
		CtClass converterClass = pool.makeClass(UUID.randomUUID().toString());

		converterClass.addMethod(CtNewMethod.make(getConverterMethodBody(cls), converterClass));
		converterClass.addMethod(CtNewMethod
				.make("public String toJson(Object o){return toJson((" + cls.getName() + ")o);}", converterClass));
		converterClass.setInterfaces(new CtClass[] { pool.get("net.piotrl.jvm.jsonassist.generation.JsonConverter") });

		JsonConverter result = (JsonConverter) pool.toClass(converterClass).newInstance();

		// this allows us to save memory
		converterClass.detach();
		return result;
	}

	// actual JSON producing code is written here!
	private String getConverterMethodBody(Class<?> cls) {
		StringBuilder sb = new StringBuilder("public String toJson(")
                .append(cls.getName())
                .append(" o) {")
                .append("return ")
                .append(new JsonObjectSerializer().serialize(cls))
                .append("; }");

        System.out.println(sb.toString());
        return sb.toString();
	}

	private String buildFieldValue(Field field, Object result) {
		if (result == null) {
			return JsonSyntaxBuilder.jsonNullValue();
		}

		return JsonStringifyFactory.factory(field)
				.apply(result);
	}
}
