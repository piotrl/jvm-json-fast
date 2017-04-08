package net.piotrl.jvm.jsonassist.example;

import java.time.LocalDate;
import java.util.function.Function;

import com.google.gson.Gson;

import net.piotrl.jvm.jsonassist.generation.Jitson;

public class Benchmark {
	public static void main(String[] args) throws Exception {
		Gson g = new Gson();
		Jitson j = new Jitson();

		Student s = new Student();
		s.id = 10;
		s.name = "Witek";

		Book b = new Book();
		b.author = "Witold Bołt";
		b.title = "JVM Internals Handbook";
		b.noPages = 778;
		b.publishedOn = LocalDate.of(2017, 10, 01);

		compare(s, j, g);
		compare(b, j, g);

	}

	private static void compare(Object o, Jitson j, Gson g) {
		long jitTime = measure(j::toJson, o);
		long googleTime = measure(g::toJson, o);
		System.out.println(o.getClass().getSimpleName() + " object: jit = " + jitTime + " google = " + googleTime
				+ " ratio = " + (double) googleTime / jitTime);
	}

	private static long measure(Function<Object, String> converter, Object o) {
		converter.apply(o);
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < 500000; i++) {
			converter.apply(o);
		}
		return System.currentTimeMillis() - t1;
	}

}
