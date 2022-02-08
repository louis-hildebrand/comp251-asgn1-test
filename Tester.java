package test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.A1_Q3;
import main.Chaining;
import main.Open_Addressing;
import test.TestHelper.Tuple;

class Chaining_chain implements Runnable {
	private List<Tuple<Integer, Integer>> testInputsOutputs = List.of(
			new Tuple<Integer, Integer>(0, 0),
			new Tuple<Integer, Integer>(1, 79),
			new Tuple<Integer, Integer>(2, 30),
			new Tuple<Integer, Integer>(3, 109),
			new Tuple<Integer, Integer>(4, 60));

	@Override
	public void run() {
		int w = 13;
		int seed = -1;
		int A = 5063;
		Chaining chaining = TestHelper.instantiateChaining(w, seed, A);

		for (Tuple<Integer, Integer> inputOutput : testInputsOutputs) {
			int expected = inputOutput.second();
			int k = inputOutput.first();
			int actual = chaining.chain(k);
			TestHelper.assertEqual(expected, actual);
		}
	}
}

class Open_Addressing_probe implements Runnable {
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
			TestHelper.assertEqual(expected, actual);
		}
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
			Chaining_chain.class,
			Open_Addressing_probe.class,
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
