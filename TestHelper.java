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

	public static void assertEqual(Object expected, Object actual, String failureMessage) {
		boolean equal;
		if (expected == null) {
			equal = actual == null;
		} else {
			equal = expected.equals(actual);
		}
		if (!equal) {
			String msg = failureMessage != null
				? failureMessage
				: String.format("Expected value %s but received value %s.", expected, actual);
			throw new AssertionError(msg);
		}
	}

	public static void assertEqualsList(List<String> expected,
		List<String> actual) {
		String failureMessage = "Expected list "
			+ TestHelper.listToString(expected) + " but received list "
			+ TestHelper.listToString(actual) + ".";
		assertEqual(expected, actual, failureMessage);
	}

	public static List<String> readLinesFromFile(String path)
		throws FileNotFoundException, IOException {
		List<String> lines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			for (String line; (line = br.readLine()) != null;) {
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
			throw new RuntimeException(
				"Failed to instantiate Chaining class: could not find constructor.",
				e);
		} catch (InstantiationException | IllegalAccessException
			| IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(
				"Failed to instantiate Chaining class: constructor threw an exception,",
				e);
		}
	}

	public static Open_Addressing instantiateOpenAddressing(int w, int seed,
		int A) {
		try {
			// Garbage code, but I don't know what else to do (except dumping
			// every file in the default package)
			Constructor<Open_Addressing> constructor = Open_Addressing.class
				.getDeclaredConstructor(int.class, int.class, int.class);
			constructor.setAccessible(true);
			return constructor.newInstance(w, seed, A);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(
				"Failed to instantiate Open_Addressing class: could not find constructor.",
				e);
		} catch (InstantiationException | IllegalAccessException
			| IllegalArgumentException | InvocationTargetException e) {
			throw new RuntimeException(
				"Failed to instantiate Open_Addressing class: constructor threw an exception,",
				e);
		}
	}

	public static void testOpenAddressing(Open_Addressing openAddressing, int[][] testInputsOutputs) {
		for (int[] test : testInputsOutputs) {
			int key = test[0];
			int expectedCollisions = test[1];
			int expectedIndex = test[2];
			String failureMessage = null;

			// Insert key
			int actualCollisions = openAddressing.insertKey(key);

			// Check that key was inserted at the right place
			int keyAtIndex = openAddressing.Table[expectedIndex];
			failureMessage = String.format("Expected to find key %d in Table[%d], but found %d.", key, expectedIndex,
				keyAtIndex);
			TestHelper.assertEqual(key, keyAtIndex, failureMessage);

			// Check that number of collisions is correct
			failureMessage = String.format("Expected %d collisions, but observed %s collisions.", expectedCollisions,
				actualCollisions);
			TestHelper.assertEqual(expectedCollisions, actualCollisions, failureMessage);
		}
	}
}
