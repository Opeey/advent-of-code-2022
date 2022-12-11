package de.opeey.aoc2022;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day03 extends Day {
	public Day03() throws FileNotFoundException, URISyntaxException {
		super(3, "157", "70");
	}

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		(new Day03()).executeTasks();
	}

	@Override
	protected String calculateTask1(List<String> input) {
		return String.valueOf(
				input.stream()
						.map(this::getDuplicateCharOrdinalInHalvesOfLine)
						.mapToInt(this::getPriorityOfCharOrdinal)
						.sum()
		);
	}

	@Override
	protected String calculateTask2(List<String> input) {
		AtomicInteger counter = new AtomicInteger();

		return String.valueOf(
				input.stream()
						.collect(Collectors.groupingBy(it -> counter.getAndIncrement() / 3)).values().stream()
						.map(this::getDuplicateCharOrdinalInThreeLines)
						.mapToInt(this::getPriorityOfCharOrdinal)
						.sum()
		);
	}

	private int getDuplicateCharOrdinalInThreeLines(List<String> lines) {
		List<List<Integer>> ordinalsInLines = lines.stream()
				.map(String::chars)
				.map(IntStream::boxed)
				.map(line -> line.collect(Collectors.toList()))
				.collect(Collectors.toList());

		return ordinalsInLines.get(0).stream()
				.filter(ordinalCharacter -> ordinalsInLines.get(1).contains(ordinalCharacter) && ordinalsInLines.get(2).contains(ordinalCharacter))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Could not find any duplicate char in 3 lines."));
	}

	private int getDuplicateCharOrdinalInHalvesOfLine(String line) {
		Stream<Integer> firstHalf = line.chars()
				.limit(line.length() / 2)
				.boxed();

		List<Integer> secondHalf = line.chars()
				.skip(line.length() / 2)
				.boxed()
				.collect(Collectors.toList());

		return firstHalf.filter(secondHalf::contains)
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Could not find any duplicate char in line."));
	}

	private int getPriorityOfCharOrdinal(int ordinal) {
		if (ordinal >= 'a') {
			return ordinal - 'a' + 1;
		}

		return ordinal - 'A' + 27;
	}
}