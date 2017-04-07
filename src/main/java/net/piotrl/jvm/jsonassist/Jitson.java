package net.piotrl.jvm.jsonassist;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewMethod;
import javassist.NotFoundException;

public class Jitson {

	private ClassPool pool;
	private Map<Class<?>, JsonConverter> cache;

	public Jitson() {
		pool = ClassPool.getDefault();
		cache = new HashMap<>();
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
		try {
			// warning: this is not thread safe!
			if (!cache.containsKey(o.getClass())) {
				cache.put(o.getClass(), getConverter(o.getClass()));
			}
			return cache.get(o.getClass()).toJson(o);
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
		converterClass.setInterfaces(new CtClass[] { pool.get("net.piotrl.jvm.jsonassist.JsonConverter") });

		JsonConverter result = (JsonConverter) pool.toClass(converterClass).newInstance();

		// this allows us to save memory
		converterClass.detach();
		return result;
	}

	// actual JSON producing code is written here!
	private String getConverterMethodBody(Class<?> cls) {
		StringBuilder sb = new StringBuilder("public String toJson(" + cls.getName() + " o) { return \"{\"+");

		// only public fields supported for now (no getter/setter support)
		sb.append(Arrays.stream(cls.getDeclaredFields())
				.map(field -> {
					try {
						return new PropertyDescriptor(field.getName(), cls);
					} catch (IntrospectionException e) {
						throw new RuntimeException(e);
					}
				})
				.map(property -> "\"\\\"" + property.getDisplayName() + "\\\":\"+o." + property.getReadMethod().getName() + "()")
				.collect(Collectors.joining("+\",\"+")));

		sb.append("+\"}\"; }");
		return sb.toString();
	}

}
