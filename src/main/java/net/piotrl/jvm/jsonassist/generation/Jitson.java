package net.piotrl.jvm.jsonassist.generation;

import javassist.*;
import net.piotrl.jvm.jsonassist.BeanFieldUtils;
import net.piotrl.jvm.jsonassist.json.JsonFactory;

import java.util.Collection;
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
		JsonConverter a = getCachedConverter(type);
		return a.toJson(o);
	}

	private JsonConverter getCachedConverter(Class<?> type) {
		if (cache.containsKey(type)) {
			return cache.get(type);
		}
		try {
			cache.put(type, generateConverter(type));
		} catch (Exception e) {
			throw new RuntimeException("Generate Converter general exception | Type: " + type.getName(), e);
		}

		return cache.get(type);
	}

	private JsonConverter generateConverter(Class<?> cls) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException {

		// new class with a random name, as this name is not needed in any way
		CtClass converterClass = pool.makeClass(UUID.randomUUID().toString());

		makeMethod(converterMethodBody(cls), converterClass);
		makeMethod(wrapperMethodBody(cls), converterClass);
		converterClass.setInterfaces(new CtClass[] { pool.get("net.piotrl.jvm.jsonassist.generation.JsonConverter") });

		JsonConverter result = (JsonConverter) pool.toClass(converterClass).newInstance();

		// this allows us to save memory
		converterClass.detach();
		return result;
	}

	private void makeMethod(String src, CtClass declaring) {
		try {
			declaring.addMethod(CtNewMethod.make(src, declaring));
		} catch (CannotCompileException e) {
			throw new RuntimeException("Make method: \n" + src, e);
		}
	}

	private String wrapperMethodBody(Class<?> cls) {
		String wrapper =  "public String toJson(Object o){return toJson((" + className(cls) + ")o);}";
		return wrapper;
	}

	// actual JSON producing code is written here!
	private String converterMethodBody(Class<?> cls) {
		StringBuilder sb = new StringBuilder("public String toJson(")
                .append(className(cls))
                .append(" o) {\n ")
                .append(JsonFactory.factory(cls).apply("o"))
                .append("; \n}");

        return sb.toString();
	}

	private String className(Class<?> cls) {
		return BeanFieldUtils.isCollection(cls) ? Collection.class.getName() : cls.getCanonicalName();
	}
}
