package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import main.Chaining;
import main.Open_Addressing;

public class TestHelper {
	public static String listToString(List<String> lst) {
		return lst == null ? "null" : "[" + String.join(", ", lst) + "]";
	}

	public static void assertEqual(Object expected, Object actual) {
		boolean equal;
		if (expected == null) {
			equal = actual == null;
		}
		else {
			equal = expected.equals(actual);
		}
		if (!equal) {
			throw new AssertionError(String.format("Expected value %s but received value %s.", expected, actual));
		}
	}

	public static void assertEqualsList(List<String> expected, List<String> actual) {
		boolean equal;
		if (expected == null) {
			equal = actual == null;
		}
		else {
			equal = expected.equals(actual);
		}
		if (!equal) {
			throw new AssertionError("Expected list " + TestHelper.listToString(expected) + " but received list "
					+ TestHelper.listToString(actual) + ".");
		}
	}

	public static List<String> readLinesFromFile(String path) throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<String>();
		try(BufferedReader br = new BufferedReader(new FileReader(path))) {
			for(String line; (line = br.readLine()) != null; ) {
				lines.add(line);
			}
		}
		return lines;
	}

	public static String getTestClassName(Class<?> cls) {
		String clsString = cls.toString();

		if (!clsString.startsWith("class test.")) {
			throw new IllegalArgumentException("Invalid class " + clsString);
		}

		return clsString.substring(11);
	}

	public static Chaining instantiateChaining(int w, int seed, int A) {
		try {
			// Garbage code, but I don't know what else to do (except dumping every file in the default package)
			Constructor<Chaining> constructor = Chaining.class.getDeclaredConstructor(int.class, int.class, int.class);
			constructor.setAccessible(true);
			return constructor.newInstance(w, seed, A);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Failed to instantiate Chaining class: could not find constructor.", e);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Failed to instantiate Chaining class: constructor threw an exception,", e);
		}
	}

	public static Open_Addressing instantiateOpenAddressing(int w, int seed, int A) {
		try {
			// Garbage code, but I don't know what else to do (except dumping every file in the default package)
			Constructor<Open_Addressing> constructor = Open_Addressing.class.getDeclaredConstructor(int.class, int.class, int.class);
			constructor.setAccessible(true);
			return constructor.newInstance(w, seed, A);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("Failed to instantiate Open_Addressing class: could not find constructor.", e);
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException("Failed to instantiate Open_Addressing class: constructor threw an exception,", e);
		}
	}

	public static class Tuple<X, Y> {
		private X first;
		private Y second;

		public Tuple(X x, Y y) {
			this.first = x;
			this.second = y;
		}

		public X first() {
			return this.first;
		}

		public Y second() {
			return this.second;
		}
	}
}
