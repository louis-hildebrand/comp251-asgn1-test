package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.A1_Q3;
import main.Chaining;
import main.Open_Addressing;

class Chaining_chain1 implements Runnable {
	private int[][] testInputsOutputs = {
		{0, 0},
		{1, 79},
		{2, 30},
		{3, 109},
		{4, 60}
	};

	@Override
	public void run() {
		int w = 13;
		int seed = -1;
		int A = 5063;
		Chaining chaining = TestHelper.instantiateChaining(w, seed, A);

		for (int[] inputOutput : testInputsOutputs) {
			int expected = inputOutput[1];
			int k = inputOutput[0];
			int actual = chaining.chain(k);
			TestHelper.assertEqual(expected, actual, null);
		}
	}
}

class Chaining_insert1 implements Runnable {
	// Each test is in the format [key, collisions, h(key)]
	private int[][] testInputsOutputs = {
		{0, 0, 0},
		{89, 1, 0},
		{3, 0, 109},
		{147, 1, 109},
		{233, 2, 0}
	};

	@Override
	public void run() {
		int w = 13;
		int seed = -1;
		int A = 5063;
		Chaining chaining = TestHelper.instantiateChaining(w, seed, A);

		for (int[] test : testInputsOutputs) {
			int expectedCollisions = test[1];
			int key = test[0];
			String failureMessage = null;

			// Insert key
			int actualCollisions = chaining.insertKey(key);

			// Check that key was inserted at the right place
			try {
				int keyAtLocation = chaining.Table.get(test[2]).get(expectedCollisions);
				failureMessage = String.format("Expected to find key %d in Table[%d][%d], but found %d.", key, test[2],
					expectedCollisions, keyAtLocation);
				TestHelper.assertEqual(key, keyAtLocation, failureMessage);
			} catch (IndexOutOfBoundsException e) {
				failureMessage = String.format(
					"Expected to find key %d in Table[%d][%d], but found no element exists at that location.", key,
					test[2], expectedCollisions);
				throw new AssertionError(failureMessage, e);
			}

			// Check that number of collisions is correct
			failureMessage = String.format("Expected %d collisions, but observed %s collisions.", expectedCollisions,
				actualCollisions);
			TestHelper.assertEqual(expectedCollisions, actualCollisions, failureMessage);
		}
	}
}

class Open_Addressing_probe1 implements Runnable {
	private int[][] testInputsOutputs = {
		{0, 0, 0},
		{0, 1, 1},
		{0, 2, 2},
		{1, 0, 79},
		{1, 1, 80},
		{1, 2, 81},
		{2, 0, 30},
		{2, 1, 31},
		{2, 2, 32},
		{2, 3, 33},
		{3, 0, 109},
		{3, 18, 127},
		{3, 19, 0},
		{3, 20, 1}
	};

	@Override
	public void run() {
		int w = 13;
		int seed = -1;
		int A = 5063;
		Open_Addressing openAddressing = TestHelper.instantiateOpenAddressing(w, seed, A);

		for (int[] inputOutput : testInputsOutputs) {
			int expected = inputOutput[2];
			int k = inputOutput[0];
			int i = inputOutput[1];
			int actual = openAddressing.probe(k, i);
			TestHelper.assertEqual(expected, actual, null);
		}
	}
}

class Open_Addressing_insert1 implements Runnable {
	// Each test is in the format [key, collisions, index of key in table]
	private int[][] testInputsOutputs = {
		{0, 0, 0},
		{89, 1, 1},
		{3, 0, 109},
		{147, 1, 110},
		{233, 2, 2},
		{55, 0, 127},
		{144, 4, 3}
	};

	@Override
	public void run() {
		int w = 13;
		int seed = -1;
		int A = 5063;
		Open_Addressing openAddressing = TestHelper.instantiateOpenAddressing(w, seed, A);

		TestHelper.testOpenAddressingInsert(openAddressing, testInputsOutputs);
	}
}

class Open_Addressing_insert_full implements Runnable {
	// Each test is in the format [key, collisions, index of key in table]
	private int[][] testInputsOutputs = {
		{4, 0, 0},
		{0, 1, 1},
		{17, 0, 3},
		{123985, 3, 2}
	};

	@Override
	public void run() {
		int w = 3;
		int seed = -1;
		int A = 6;
		Open_Addressing openAddressing = TestHelper.instantiateOpenAddressing(w, seed, A);

		// Test first few insertions normally
		TestHelper.testOpenAddressingInsert(openAddressing, testInputsOutputs);

		// Try inserting into full table
		int numCollisions = openAddressing.insertKey(5);

		// Method should check all cells before giving up
		String failureMessage = String.format("Expected 4 collisions, but observed %s collisions.", numCollisions);
		TestHelper.assertEqual(4, numCollisions, failureMessage);

		// Table should be unaffected by the operation
		int[] table = openAddressing.Table;
		TestHelper.assertEqual(4, table[0], "Table[0] changed from 4 to" + table[0] + ".");
		TestHelper.assertEqual(0, table[1], "Table[1] changed from 0 to" + table[1] + ".");
		TestHelper.assertEqual(123985, table[2], "Table[2] changed from 123985 to" + table[2] + ".");
		TestHelper.assertEqual(17, table[3], "Table[3] changed from 17 to" + table[3] + ".");
	}
}

class Open_Addressing_remove1 implements Runnable {
	// Each test is in the format [key, index (or negative number if not applicable), optimal #collisions]
	private int[][] testInputsOutputs = {
		{108, -1, 1},
		{89, 1, 1},
		{144, 3, 4},
		{144, -1, 6}
	};

	@Override
	public void run() {
		int w = 13;
		int seed = -1;
		int A = 5063;
		Open_Addressing openAddressing = TestHelper.instantiateOpenAddressing(w, seed, A);
		int[] table = openAddressing.Table;
		table[0] = 0;
		table[1] = 89;
		table[2] = 233;
		table[3] = 144;
		table[109] = 3;
		table[110] = 147;
		table[127] = 55;

		TestHelper.testOpenAddressingRemove(openAddressing, testInputsOutputs);

		// Check that non-removed keys are still there
		TestHelper.assertEqual(0, table[0], "Table[0] was changed (current value is " + table[0] + ").");
		TestHelper.assertEqual(233, table[2], "Table[2] was changed (current value is " + table[2] + ").");
		TestHelper.assertEqual(3, table[109], "Table[109] was changed (current value is " + table[109] + ").");
		TestHelper.assertEqual(147, table[110], "Table[110] was changed (current value is " + table[110] + ").");
		TestHelper.assertEqual(55, table[127], "Table[127] was changed (current value is " + table[127] + ").");
	}
}

class Open_Addressing_remove_full implements Runnable {
	// Each test is in the format [key, index (or negative number if not applicable), optimal #collisions]
	private int[][] testInputsOutputs = {
		{3, -1, 4},
		{123985, 2, 3},
		{42, -1, 4}
	};

	@Override
	public void run() {
		int w = 3;
		int seed = -1;
		int A = 6;
		Open_Addressing openAddressing = TestHelper.instantiateOpenAddressing(w, seed, A);
		int[] table = openAddressing.Table;
		table[0] = 4;
		table[1] = 0;
		table[2] = 123985;
		table[3] = 17;

		TestHelper.testOpenAddressingRemove(openAddressing, testInputsOutputs);

		// Check that non-removed keys are still there
		TestHelper.assertEqual(4, table[0], "Table[0] was changed (current value is " + table[0] + ").");
		TestHelper.assertEqual(0, table[1], "Table[1] was changed (current value is " + table[1] + ").");
		TestHelper.assertEqual(17, table[3], "Table[3] was changed (current value is " + table[3] + ").");
	}
}

class Q3_empty implements Runnable {
	@Override
	public void run() {
		String[] messages = new String[0];
		List<String> expected = new ArrayList<String>();

		List<String> actual = A1_Q3.Discussion_Board(messages);
		TestHelper.assertEqualsList(expected, actual);
	}
}

class Q3_no_output implements Runnable {
	@Override
	public void run() {
		String[] messages = new String[]{"David comp", "Maria music"};
		List<String> expected = new ArrayList<String>();

		List<String> actual = A1_Q3.Discussion_Board(messages);
		TestHelper.assertEqualsList(expected, actual);
	}
}

class Q3_example1 implements Runnable {
	@Override
	public void run() {
		String[] messages = new String[]{"David no no no no nobody never",
			"Jennifer why ever not",
			"Parham no not never nobody", "Shishir no never know nobody",
			"Alvin why no nobody",
			"Alvin nobody never know why nobody", "David never no nobody",
			"Jennifer never never nobody no"};
		List<String> expected = Arrays
			.asList(new String[]{"no", "nobody", "never"});

		List<String> actual = A1_Q3.Discussion_Board(messages);
		TestHelper.assertEqualsList(expected, actual);
	}
}

class Q3_same_frequency implements Runnable {
	@Override
	public void run() {
		// Same example as in the instructions, but with a few words added so
		// that the three words have the same frequency
		String[] messages = new String[]{
			"David no no no no nobody never nobody never never alpha",
			"Jennifer why ever not alpha",
			"Parham no not never nobody alpha",
			"Shishir no never know nobody alpha",
			"Alvin why no nobody alpha",
			"Alvin nobody never know why nobody",
			"David never no nobody", "Jennifer never never nobody no"
		};
		List<String> expected = Arrays
			.asList(new String[]{"never", "no", "nobody", "alpha"});

		List<String> actual = A1_Q3.Discussion_Board(messages);
		TestHelper.assertEqualsList(expected, actual);
	}
}

class Q3_large_input1 implements Runnable {
	@Override
	public void run() {
		String inputPath = "src/test/data/Q3_large_input1_in.txt";
		String outputPath = "src/test/data/Q3_large_input1_out.txt";
		try {
			List<String> messagesList = TestHelper.readLinesFromFile(inputPath);
			String[] messages = new String[messagesList.size()];
			messages = messagesList.toArray(messages);
			List<String> expected = TestHelper.readLinesFromFile(outputPath);

			List<String> actual = A1_Q3.Discussion_Board(messages);
			TestHelper.assertEqualsList(expected, actual);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

public class Tester {
	private static Class<?>[] testClasses = {
		Chaining_chain1.class,
		Chaining_insert1.class,
		Open_Addressing_probe1.class,
		Open_Addressing_insert1.class,
		Open_Addressing_insert_full.class,
		Open_Addressing_remove1.class,
		Open_Addressing_remove_full.class,
		Q3_empty.class,
		Q3_no_output.class,
		Q3_example1.class,
		Q3_same_frequency.class,
		Q3_large_input1.class
	};

	public static void main(String[] args) {
		ArrayList<Class<?>> failedTests = new ArrayList<Class<?>>(
			testClasses.length);
		for (Class<?> cls : testClasses) {
			System.out.printf("%n======= %s =======%n",
				TestHelper.getTestClassName(cls));
			System.out.flush();
			try {
				Runnable testCase = (Runnable) cls.getDeclaredConstructor()
					.newInstance();
				testCase.run();
			} catch (AssertionError e) {
				System.out.println(e);
				failedTests.add(cls);
			} catch (StackOverflowError e) {
				StackTraceElement[] elements = e.getStackTrace();
				System.out.println(
					cls.toString() + " caused a stack overflow at: ");
				for (int i = 0; i < 5 && i < elements.length; i++) {
					System.out.println(elements[i]);
				}
				if (elements.length >= 5) {
					System.out.println("...and " + (elements.length - 5)
						+ " more elements.");
				}
				failedTests.add(cls);
			} catch (Throwable t) {
				t.printStackTrace();
				failedTests.add(cls);
			}
		}

		int numPassed = testClasses.length - failedTests.size();
		System.out.printf("%n%n%d of %d tests passed.%n", numPassed,
			testClasses.length);
		if (failedTests.size() > 0) {
			System.out.printf("Failed test%s:\n",
				failedTests.size() > 1 ? "s" : "");
			for (Class<?> cls : failedTests) {
				String className = TestHelper.getTestClassName(cls);
				System.out.println("- " + className);
			}
		} else {
			System.out.println("All clear! Great work :)");
		}
	}
}
