package de.opeey.aoc2022;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class Day02 extends Day {
	public Day02() throws FileNotFoundException, URISyntaxException {
		super(2, "15", "12");
	}

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		(new Day02()).executeTasks();
	}

	@Override
	protected String calculateTask1(List<String> input) {
		return String.valueOf(input.stream()
				.map(this::getGameForMovesInLine)
				.mapToInt(RockPaperScissorGame::calculateScore)
				.sum()
		);
	}

	@Override
	protected String calculateTask2(List<String> input) {
		return String.valueOf(input.stream()
				.map(this::getGameForPredefinedResult)
				.mapToInt(RockPaperScissorGame::calculateScore)
				.sum()
		);
	}

	private RockPaperScissorGame getGameForPredefinedResult(String line) {
		List<String> lineInformation = Arrays.stream(line.split(" "))
				.collect(Collectors.toList());

		RockPaperScissorMove opponentMove = RockPaperScissorMove.valueOf(lineInformation.get(0));
		RockPaperScissorGameResult desiredResult = RockPaperScissorGameResult.valueOf(lineInformation.get(1));

		return new RockPaperScissorGame(opponentMove, desiredResult);
	}

	private RockPaperScissorGame getGameForMovesInLine(String line) {
		List<RockPaperScissorMove> rockPaperScissorMoves = Arrays.stream(line.split(" "))
				.map(RockPaperScissorMove::valueOf)
				.collect(Collectors.toList());

		return new RockPaperScissorGame(rockPaperScissorMoves.get(0), rockPaperScissorMoves.get(1));
	}

	@RequiredArgsConstructor
	private enum RockPaperScissorMove {
		A("Rock", 1),
		X("Rock", 1),

		B("Paper", 2),
		Y("Paper", 2),

		C("Scissors", 3),
		Z("Scissors", 3);

		public final String label;
		public final Integer value;
	}

	@RequiredArgsConstructor
	private enum RockPaperScissorGameResult {
		X("Lose", 0),
		Y("Draw", 3),
		Z("Win", 6);

		public final String label;
		public final Integer value;
	}

	@RequiredArgsConstructor
	@Data
	private class RockPaperScissorGame {
		private final List<RockPaperScissorMove> moveList = List.of(
				RockPaperScissorMove.A,
				RockPaperScissorMove.B,
				RockPaperScissorMove.C
		);
		private final RockPaperScissorMove opponentMove;
		private final RockPaperScissorMove ownMove;

		public RockPaperScissorGame(RockPaperScissorMove opponentMove, RockPaperScissorGameResult desiredResult) {
			this.opponentMove = opponentMove;
			ownMove = getMoveForDesiredResult(opponentMove, desiredResult);
		}

		public int calculateScore() {
			return ownMove.value + calculateGameScore().value;
		}

		private RockPaperScissorGameResult calculateGameScore() {
			int valueDifference = ownMove.value - opponentMove.value;

			if (valueDifference == 0) {
				return RockPaperScissorGameResult.Y;
			}

			if (valueDifference == -2 || valueDifference == 1) {
				return RockPaperScissorGameResult.Z;
			}

			return RockPaperScissorGameResult.X;
		}

		private RockPaperScissorMove getMoveForDesiredResult(RockPaperScissorMove opponentMove, RockPaperScissorGameResult desiredResult) {
			if (desiredResult.label.equals("Win")) {
				return getWinningMove(opponentMove);
			}

			if (desiredResult.label.equals("Lose")) {
				return getLosingMove(opponentMove);
			}

			return opponentMove;
		}

		private RockPaperScissorMove getWinningMove(RockPaperScissorMove move) {
			int winningIndex = moveList.indexOf(move) + 1;

			if (winningIndex >= moveList.size()) {
				winningIndex = 0;
			}

			return moveList.get(winningIndex);
		}

		private RockPaperScissorMove getLosingMove(RockPaperScissorMove move) {
			int losingIndex = moveList.indexOf(move) - 1;

			if (losingIndex < 0) {
				losingIndex = moveList.size() - 1;
			}

			return moveList.get(losingIndex);
		}
	}
}