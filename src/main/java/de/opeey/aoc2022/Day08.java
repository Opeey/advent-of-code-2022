package de.opeey.aoc2022;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day08 extends Day {
	public Day08() throws FileNotFoundException, URISyntaxException {
		super(8, "21", "8");
	}

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		(new Day08()).executeTasks();
	}

	@Override
	protected String calculateTask1(List<String> input) {
		List<List<List<Integer>>> rowsAndColumns = getRowsAndColumnsOfLines(input);
		List<List<Integer>> rows = rowsAndColumns.get(0);
		List<List<Integer>> columns = rowsAndColumns.get(1);

		// top row + bottom row + left col + right col - 4 for duplicate corners
		Integer visibleTrees = rows.size() * 2 + columns.size() * 2 - 4;

		for (int rowIdx : IntStream.range(1, rows.size() - 1).toArray()) {
			List<Integer> row = rows.get(rowIdx);

			for (int colIdx : IntStream.range(1, row.size() - 1).toArray()) {
				List<Integer> column = columns.get(colIdx);

				List<Integer> top = column.subList(0, rowIdx);
				List<Integer> bottom = column.subList(rowIdx + 1, column.size());
				List<Integer> left = row.subList(0, colIdx);
				List<Integer> right = row.subList(colIdx + 1, row.size());

				Integer tree = row.get(colIdx);

				if (areOnlySmallerNumbersInList(top, tree) || areOnlySmallerNumbersInList(bottom, tree) || areOnlySmallerNumbersInList(left, tree) || areOnlySmallerNumbersInList(right, tree)) {
					visibleTrees++;
				}
			}
		}

		return String.valueOf(visibleTrees);
	}

	@Override
	protected String calculateTask2(List<String> input) {
		List<List<List<Integer>>> rowsAndColumns = getRowsAndColumnsOfLines(input);
		List<List<Integer>> rows = rowsAndColumns.get(0);
		List<List<Integer>> columns = rowsAndColumns.get(1);

		List<Integer> scenicScores = new ArrayList<>();
		for (int rowIdx : IntStream.range(1, rows.size() - 1).toArray()) {
			List<Integer> row = rows.get(rowIdx);

			for (int colIdx : IntStream.range(1, row.size() - 1).toArray()) {
				List<Integer> column = columns.get(colIdx);

				List<Integer> top = column.subList(0, rowIdx);
				List<Integer> bottom = column.subList(rowIdx + 1, column.size());
				List<Integer> left = row.subList(0, colIdx);
				List<Integer> right = row.subList(colIdx + 1, row.size());

				Collections.reverse(top);
				Collections.reverse(left);

				int tree = row.get(colIdx);
				int score = Stream.of(top, bottom, left, right)
						.mapToInt(list -> calculateScenicScoreOfList(list, tree))
						.reduce(1, (a, b) -> a * b);

				scenicScores.add(score);

				Collections.reverse(top);
				Collections.reverse(left);
			}
		}

		return String.valueOf(
				scenicScores.stream()
						.mapToInt(Integer::intValue)
						.max()
						.orElse(0)
		);
	}

	private boolean areOnlySmallerNumbersInList(List<Integer> list, Integer number) {
		return list.stream().noneMatch(otherNumber -> otherNumber >= number);
	}

	private int calculateScenicScoreOfList(List<Integer> list, Integer number) {
		int scenicScore = 0;
		for (int otherNumber : list) {
			scenicScore++;
			if (otherNumber >= number) {
				break;
			}
		}

		return scenicScore;
	}

	private List<List<List<Integer>>> getRowsAndColumnsOfLines(List<String> lines) {
		List<List<Integer>> rows = new ArrayList<>();
		List<List<Integer>> columns = new ArrayList<>();
		for (int rowIdx : IntStream.range(0, lines.size()).toArray()) {
			List<Integer> integersInRow = getIntegersOfLine(lines.get(rowIdx));
			rows.add(integersInRow);

			for (int colIdx : IntStream.range(0, integersInRow.size()).toArray()) {
				if (columns.size() <= colIdx) {
					columns.add(colIdx, new ArrayList<>());
				}
				columns.get(colIdx).add(integersInRow.get(colIdx));
			}
		}

		return List.of(rows, columns);
	}

	private List<Integer> getIntegersOfLine(String line) {
		List<Integer> listOfIntegers = new ArrayList<>();

		for (char ch : line.toCharArray()) {
			listOfIntegers.add(Integer.parseInt(String.valueOf(ch)));
		}

		return listOfIntegers;
	}
}