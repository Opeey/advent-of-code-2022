package de.opeey.aoc2022;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class Day06 extends Day {
	public Day06() throws FileNotFoundException, URISyntaxException {
		super(6, "7", "19");
	}

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		(new Day06()).executeTasks();
	}

	@Override
	protected String calculateTask1(List<String> input) {
		return String.valueOf(getPositionOfUniqueCharactersInLine(input.get(0), 4));
	}

	@Override
	protected String calculateTask2(List<String> input) {
		return String.valueOf(getPositionOfUniqueCharactersInLine(input.get(0), 14));
	}

	private int getPositionOfUniqueCharactersInLine(String line, int numberOfCharacters) {
		AtomicInteger currentWindowIndex = new AtomicInteger(numberOfCharacters);
		AtomicReference<String> currentWindow = new AtomicReference<>(line.substring(0, currentWindowIndex.get()));

		IntStream.range(numberOfCharacters - 1, line.length()).forEach(idx -> {
			if (currentWindow.get().chars().boxed().distinct().count() == numberOfCharacters) {
				return;
			}
			currentWindow.set(currentWindow.get().substring(1) + line.charAt(idx + 1));
			currentWindowIndex.incrementAndGet();
		});

		return currentWindowIndex.get();
	}
}