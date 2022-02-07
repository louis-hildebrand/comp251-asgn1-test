package test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {
	public static String listToString(List<String> lst) {
		return lst == null ? "null" : "[" + String.join(", ", lst) + "]";
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
}
