package de.opeey.aoc2022;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import lombok.Data;
import lombok.RequiredArgsConstructor;

public class Day07 extends Day {
	private final int TOTAL_DISK_SIZE = 70000000;
	private final int UPDATE_SIZE = 30000000;

	public Day07() throws FileNotFoundException, URISyntaxException {
		super(7, "95437", "24933642");
	}

	public static void main(String[] args) throws FileNotFoundException, URISyntaxException {
		(new Day07()).executeTasks();
	}

	@Override
	protected String calculateTask1(List<String> input) {
		Directory rootDirectory = parseLinesIntoDirectoryStructure(input);
		System.out.printf("%nGot file-tree:%n");
		printDirectoryStructure(rootDirectory);

		return String.valueOf(
				rootDirectory.getAllDirectoriesInTree().stream()
						.filter(directory -> directory.getSize() <= 100000)
						.mapToInt(Directory::getSize)
						.sum()
		);
	}

	@Override
	protected String calculateTask2(List<String> input) {
		Directory rootDirectory = parseLinesIntoDirectoryStructure(input);
		System.out.printf("%nGot file-tree:%n");
		printDirectoryStructure(rootDirectory);

		int unusedSpace = TOTAL_DISK_SIZE - rootDirectory.getSize();
		int spaceToDelete = UPDATE_SIZE - unusedSpace;

		return String.valueOf(
				rootDirectory.getAllDirectoriesInTree().stream()
						.mapToInt(Directory::getSize)
						.filter(sizeOfDirectory -> sizeOfDirectory >= spaceToDelete)
						.min()
						.orElse(0)
		);
	}

	private void printDirectoryStructure(Directory directory) {
		printDirectoryStructure(directory, 0);
	}

	private void printDirectoryStructure(Directory directory, int depth) {
		String indentation = new String(new char[depth]).replace("\0", " ");
		System.out.printf("%s- %s (dir)%n", indentation, directory.getName());
		directory.getSubDirectories().forEach(subDirectory -> printDirectoryStructure(subDirectory, depth + 1));
		directory.getFiles().forEach(file -> System.out.printf("%s- %s (file, size=%d)%n", indentation, file.getName(), file.getSize()));
	}

	private Directory parseLinesIntoDirectoryStructure(List<String> lines) {
		AtomicReference<Directory> currentDirectory = new AtomicReference<>();

		lines.forEach(line -> {
			if (line.startsWith("$ cd")) {
				String directoryName = line.substring(5);

				if (currentDirectory.get() == null) {
					currentDirectory.set(new Directory(directoryName, null));
					return;
				}

				if (directoryName.equals("..")) {
					currentDirectory.set(currentDirectory.get().getParentDirectory());
				} else {
					currentDirectory.set(currentDirectory.get().getSubDirectory(directoryName)
							.orElseThrow(() -> new RuntimeException(String.format(
									"Sub-directory %s of %s could not be found.",
									directoryName,
									currentDirectory.get().getName())
							))
					);
				}
			} else if (line.startsWith("dir ")) {
				currentDirectory.get().addSubDirectory(new Directory(line.substring(4), currentDirectory.get()));
			} else if (!line.startsWith("$ ls")) {
				String[] partsOfLine = line.split(" ");
				currentDirectory.get().addFile(new File(partsOfLine[1], Integer.parseInt(partsOfLine[0])));
			}
		});

		while (currentDirectory.get().hasParent()) {
			currentDirectory.set(currentDirectory.get().getParentDirectory());
		}

		return currentDirectory.get();
	}

	@Data
	private static class Directory {
		private final String name;
		private final Directory parentDirectory;
		private final List<Directory> subDirectories = new ArrayList<>();
		private final List<File> files = new ArrayList<>();

		public Directory(String name, Directory parentDirectory) {
			this.name = name;
			this.parentDirectory = parentDirectory;
		}

		public boolean hasParent() {
			return parentDirectory != null;
		}

		public Optional<Directory> getSubDirectory(String name) {
			return subDirectories.stream()
					.filter(directory -> directory.getName().equals(name))
					.findFirst();
		}

		public List<Directory> getAllDirectoriesInTree() {
			List<Directory> directoryList = new ArrayList<>(List.of(this));
			getSubDirectories().forEach(subDirectory -> directoryList.addAll(subDirectory.getAllDirectoriesInTree()));
			return directoryList;
		}

		public Directory addSubDirectory(Directory subDirectory) {
			subDirectories.add(subDirectory);

			return this;
		}

		public Directory addFile(File file) {
			files.add(file);

			return this;
		}

		public int getSize() {
			return subDirectories.stream()
					.mapToInt(Directory::getSize)
					.sum() + files.stream()
					.mapToInt(File::getSize)
					.sum();
		}
	}

	@RequiredArgsConstructor
	@Data
	private static class File {
		private final String name;
		private final int size;
	}
}