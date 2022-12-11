package de.opeey.aoc2022;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day01 extends Day {
	public Day01() throws FileNotFoundException, URISyntaxException {
		super(1, "24000", "45000");
	}

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		(new Day01()).executeTasks();
	}

	@Override
	protected String calculateTask1(List<String> input) {
		return String.valueOf(
				groupLinesAndCastToInteger(input).stream()
						.mapToInt(foodItems -> foodItems.stream().reduce(Integer::sum).orElse(0))
						.max()
						.orElse(0)
		);
	}

	@Override
	protected String calculateTask2(List<String> input) {
		return String.valueOf(
				groupLinesAndCastToInteger(input).stream()
						.mapToInt(foodItems -> foodItems.stream().reduce(Integer::sum).orElse(0))
						.boxed()
						.sorted(Comparator.reverseOrder())
						.limit(3)
						.mapToInt(Integer::intValue)
						.sum()
		);
	}

	private List<List<Integer>> groupLinesAndCastToInteger(List<String> lines) {
		List<List<Integer>> groupedLines = new ArrayList<>();

		List<Integer> currentLine = new ArrayList<>();
		for (String line : lines) {
			if (line.equals("")) {
				if (!currentLine.isEmpty()) {
					groupedLines.add(currentLine);
					currentLine = new ArrayList<>();
				}
			} else {
				currentLine.add(Integer.valueOf(line));
			}
		}

		if (!currentLine.isEmpty()) {
			groupedLines.add(currentLine);
		}

		return groupedLines;
	}
}