package net.piotrl.jvm.jsonassist;

/**
 * Helper interface implemented automatically by Javaassist.
 */
public interface JsonConverter {
	String toJson(Object o);
}