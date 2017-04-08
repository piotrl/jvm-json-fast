package net.piotrl.jvm.jsonassist.generation;

/**
 * Helper interface implemented automatically by Javaassist.
 */
public interface JsonConverter {
	String toJson(Object o);
}