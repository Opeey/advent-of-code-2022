package de.opeey.aoc2022;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class Day05 extends Day {
	public Day05() throws FileNotFoundException, URISyntaxException {
		super(5, "CMZ", "MCD");
	}

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		(new Day05()).executeTasks();
	}

	@Override
	protected String calculateTask1(List<String> input) {
		int stackNumberLine = getIndexOfStackNumberLine(input);

		List<Stack<String>> stacks = getStacksForCrateLines(input.stream().limit(stackNumberLine));

		System.out.printf("%nInput stacks:%n");
		printStacks(stacks);
		System.out.printf("%n");

		input.stream()
				.skip(stackNumberLine + 2L)
				.map(this::parseLineToMoveOperation)
				.forEach(moveOperation -> IntStream.range(0, moveOperation.numberOfItems).boxed()
						.forEach(idx -> stacks.get(moveOperation.toPosition - 1).push(stacks.get(moveOperation.fromPosition - 1).pop())));

		System.out.printf("%nOutput stacks:%n");
		printStacks(stacks);
		System.out.printf("%n");

		return stacks.stream()
				.map(Stack::pop)
				.collect(Collectors.joining());
	}

	@Override
	protected String calculateTask2(List<String> input) {
		int stackNumberLine = getIndexOfStackNumberLine(input);

		List<Stack<String>> stacks = getStacksForCrateLines(input.stream().limit(stackNumberLine));

		System.out.printf("%nInput stacks:%n");
		printStacks(stacks);
		System.out.printf("%n");

		input.stream()
				.skip(stackNumberLine + 2L)
				.map(this::parseLineToMoveOperation)
				.forEach(moveOperation -> {
					List<String> itemsToMove = new ArrayList<>();
					IntStream.range(0, moveOperation.numberOfItems).boxed()
							.forEach(it -> itemsToMove.add(stacks.get(moveOperation.fromPosition - 1).pop()));

					Collections.reverse(itemsToMove);
					itemsToMove.forEach(crate -> stacks.get(moveOperation.toPosition - 1).push(crate));
				});

		System.out.printf("%nOutput stacks:%n");
		printStacks(stacks);
		System.out.printf("%n");

		return stacks.stream()
				.map(Stack::pop)
				.collect(Collectors.joining());
	}

	private int getIndexOfStackNumberLine(List<String> lines) {
		String stackNumbers = lines.stream()
				.filter(line -> line.startsWith(" 1"))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("Could not find stack numbers in input!"));

		return lines.indexOf(stackNumbers);
	}

	private List<Stack<String>> getStacksForCrateLines(Stream<String> crateLines) {
		List<Stack<String>> stacks = new ArrayList<>();

		crateLines.map(this::getCratesInLine)
				.forEach(crates -> {
					for (int index = 0; index < crates.size(); index++) {
						if (stacks.size() <= index) {
							stacks.add(new Stack<>());
						}

						String crate = crates.get(index);
						if (!Objects.equals(crate, " ")) {
							stacks.get(index).push(crate);
						}
					}
				});

		return stacks.stream()
				.map(this::reverseStack)
				.collect(Collectors.toList());
	}

	private Stack<String> reverseStack(Stack<String> stack) {
		Stack<String> reversedStack = new Stack<>();

		while (!stack.empty()) {
			reversedStack.push(stack.pop());
		}

		return reversedStack;
	}

	private List<String> getCratesInLine(String line) {
		Pattern pattern = Pattern.compile(" ( ) |\\[(.)\\]");
		return pattern.matcher(line).results()
				.map(matchResult -> {
					if (matchResult.group(1) != null) {
						return matchResult.group(1);
					} else {
						return matchResult.group(2);
					}
				})
				.map(crateString -> crateString.equals("") ? null : crateString)
				.collect(Collectors.toList());
	}

	private MoveOperation parseLineToMoveOperation(String line) {
		Pattern pattern = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");
		Matcher matcher = pattern.matcher(line);

		if (matcher.find()) {
			return new MoveOperation(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
		}

		throw new RuntimeException("Could not parse line to move operation.");
	}

	private void printStacks(List<Stack<String>> stacks) {
		List<String> messages = new ArrayList<>();
		messages.add(" " + IntStream.range(1, stacks.size() + 1).boxed().map(String::valueOf).collect(Collectors.joining("   ")) + " ");

		List<Stack<String>> reversedStacks = stacks.stream()
				.map(stack -> (Stack<String>) stack.clone())
				.map(this::reverseStack)
				.collect(Collectors.toList());

		IntStream.range(0, stacks.stream().mapToInt(Stack::size).max().orElse(0)).boxed()
				.forEach(idx -> {
					List<String> boxes = new ArrayList<>();
					reversedStacks.forEach(stack -> {
						if (!stack.isEmpty()) {
							boxes.add("[" + stack.pop() + "]");
						} else {
							boxes.add("   ");
						}
					});
					messages.add(String.join(" ", boxes));
				});

		Collections.reverse(messages);
		messages.forEach(System.out::println);
	}

	@RequiredArgsConstructor
	@Data
	private static class MoveOperation {
		private final int numberOfItems;
		private final int fromPosition;
		private final int toPosition;

		public String toString() {
			return String.format("#%d %d -> %d", numberOfItems, fromPosition, toPosition);
		}
	}
}