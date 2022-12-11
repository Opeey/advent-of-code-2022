package de.opeey.aoc2022;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public abstract class Day {
	protected final List<String> testInputLines;
	protected final String task1TestSolution;
	protected final String task2TestSolution;
	protected final List<String> puzzleInputLines;
	private final Integer numberOfDay;

	public Day(Integer numberOfDay, String task1TestSolution, String task2TestSolution) throws URISyntaxException, FileNotFoundException {
		this.numberOfDay = numberOfDay;
		this.task1TestSolution = task1TestSolution;
		this.task2TestSolution = task2TestSolution;

		File testInputFile = getFileFromResource(String.format("input/day%02d/test", numberOfDay));
		testInputLines = readLinesFromFile(testInputFile);

		File puzzleInputFile = getFileFromResource(String.format("input/day%02d/puzzle", numberOfDay));
		puzzleInputLines = readLinesFromFile(puzzleInputFile);
	}

	private File getFileFromResource(String fileName) throws URISyntaxException {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("File not found! " + fileName);
		} else {
			return new File(resource.toURI());
		}
	}

	private List<String> readLinesFromFile(File file) throws FileNotFoundException {
		Scanner scanner = new Scanner(file);
		List<String> lines = new ArrayList<>();

		while (scanner.hasNextLine()) {
			lines.add(scanner.nextLine());
		}

		scanner.close();

		return lines;
	}

	public void executeTasks() {
		System.out.printf("Executing tasks for day %d.%n", numberOfDay);

		System.out.printf("Testing calculation of task 1 with solution %s.%n", task1TestSolution);
		String calculationTask1 = calculateTask1(testInputLines);

		if (!Objects.equals(calculationTask1, task1TestSolution)) {
			System.err.printf("Calculation did not match desired solution. Expected %s, but got %s.%n", task1TestSolution, calculationTask1);
			return;
		}
		System.out.printf("Success!%n%n");

		System.out.printf("Calculating solution for puzzle input of task 1.%n");
		System.out.printf("Solution of puzzle for task 1 is: %s", calculateTask1(puzzleInputLines));
		System.out.printf("%n%n");

		if (task2TestSolution == null) {
			System.err.printf("Solution for task 2 is not available.%n");
			return;
		}

		System.out.printf("Testing calculation of task 2 with solution %s.%n", task2TestSolution);
		String calculationTask2 = calculateTask2(testInputLines);

		if (!Objects.equals(calculationTask2, task2TestSolution)) {
			System.err.printf("Calculation did not match desired solution. Expected %s, but got %s.%n", task2TestSolution, calculationTask2);
			return;
		}
		System.out.printf("Success!%n%n");

		System.out.printf("Calculating solution for puzzle input of task 2.%n");
		System.out.printf("Solution of puzzle for task 2 is: %s", calculateTask2(puzzleInputLines));
		System.out.printf("%n%n");
	}

	protected abstract String calculateTask1(List<String> input);

	protected abstract String calculateTask2(List<String> input);
}
