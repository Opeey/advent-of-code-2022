package de.opeey.aoc2022;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day04 extends Day {
	public Day04() throws FileNotFoundException, URISyntaxException {
		super(4, "2", "4");
	}

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		(new Day04()).executeTasks();
	}

	@Override
	protected String calculateTask1(List<String> input) {
		return String.valueOf(
				input.stream()
						.map(this::getPairOfRangesFromLine)
						.map(rangePair -> rangePair.get(0).stream()
								.anyMatch(section -> !rangePair.get(1).contains(section)) &&
								rangePair.get(1).stream()
										.anyMatch(section -> !rangePair.get(0).contains(section))
						).mapToInt(listsDontIntersect -> Boolean.TRUE.equals(listsDontIntersect) ? 0 : 1)
						.sum()
		);
	}

	@Override
	protected String calculateTask2(List<String> input) {
		return String.valueOf(
				input.stream()
						.map(this::getPairOfRangesFromLine)
						.map(rangePair -> rangePair.get(0).stream()
								.anyMatch(section -> rangePair.get(1).contains(section)) ||
								rangePair.get(1).stream()
										.anyMatch(section -> rangePair.get(0).contains(section))
						).mapToInt(listsDoIntersect -> Boolean.TRUE.equals(listsDoIntersect) ? 1 : 0)
						.sum()
		);
	}

	private List<List<Integer>> getPairOfRangesFromLine(String line) {
		return Arrays.stream(line.split(","))
				.map(rangeString -> {
					List<Integer> sectionBoundaries = Arrays.stream(rangeString.split("-"))
							.map(Integer::valueOf)
							.collect(Collectors.toList());

					return IntStream.range(sectionBoundaries.get(0), sectionBoundaries.get(1) + 1).boxed()
							.collect(Collectors.toList());
				})
				.collect(Collectors.toList());
	}
}