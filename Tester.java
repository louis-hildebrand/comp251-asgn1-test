package test;

import java.util.ArrayList;

class Q1_1 implements Runnable {
	@Override
	public void run() {
		throw new AssertionError("Test failed");
	}
}

public class Tester {
	private static String[] tests = {
			"test.Q1_1"
	};

	public static void main(String[] args) {
		ArrayList<String> failedTests = new ArrayList<String>(tests.length);
		for (String className : tests) {
			System.out.printf("%n======= %s =======%n", className);
			System.out.flush();
			try {
				Runnable testCase = (Runnable) Class.forName(className).getDeclaredConstructor().newInstance();
				testCase.run();
			} catch (AssertionError e) {
				System.out.println(e);
				failedTests.add(className);
			} catch (StackOverflowError e) {
				StackTraceElement[] elements = e.getStackTrace();
				System.out.println(className + " caused a stack overflow at: ");
				for (int i = 0; i < 5 && i < elements.length; i++) {
					System.out.println(elements[i]);
				}
				if (elements.length >= 5) {
					System.out.println("...and " + (elements.length - 5) + " more elements.");
				}
				failedTests.add(className);
			} catch (Throwable t) {
				t.printStackTrace();
				failedTests.add(className);
			}
		}

		int numPassed = tests.length - failedTests.size();
		System.out.printf("%n%n%d of %d tests passed.%n", numPassed, tests.length);
		if (failedTests.size() > 0) {
			System.out.printf("Failed test%s:\n", failedTests.size() > 1 ? "s" : "");
			for (String className : failedTests) {
				int dotIndex = className.indexOf('.');
				System.out.println("- " + className.substring(dotIndex + 1));
			}
		} else {
			System.out.println("All clear! Great work :)");
		}
	}
}
