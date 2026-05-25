package app;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dictionary.WordLoader;
import word.Word;

public class DictionaryBuilder {

	private static final String GOALFILE = "goals.txt";
	private static final String ALLOWEDFILE = "allowed_words.txt";
	private static final String SOLUTIONFILE = "solutions.txt";
	private static final String WORDLEBOT = "wordlebot.txt";

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		rebuildDictionaries(
				Path.of(GOALFILE),
				Path.of(ALLOWEDFILE),
				Path.of(SOLUTIONFILE),
				Path.of(WORDLEBOT),
				Path.of("goals.txt"),
				Path.of("past_solutions.txt")
		);

		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
	}

	/**
	 * Package-private so tests in the same package can call it.
	 * This method performs all the real work. main() is now just a wrapper.
	 */
	static void rebuildDictionaries(
			Path goalFile,
			Path allowedFile,
			Path solutionFile,
			Path wordlebotFile,
			Path outGoalsFile,
			Path outPastSolutionsFile) {

		WordLoader loader = new WordLoader();

		List<Word> glist = loader.loadWords(goalFile.toString());
		List<Word> alist = loader.loadWords(allowedFile.toString());
		List<Word> slist = loader.loadWords(solutionFile.toString());
		List<Word> wlist = loader.loadWords(wordlebotFile.toString());

		System.out.println(goalFile + " has " + glist.size() + " words.");
		System.out.println(allowedFile + " has " + alist.size() + " words.");
		System.out.println(solutionFile + " has " + slist.size() + " words.");

		// Build new goals list: union of goals + wordlebot
		Set<Word> aset = new HashSet<>(glist);
		aset.addAll(wlist);
		loader.writeWords(new ArrayList<>(aset), outGoalsFile.toString());

		// Build past solutions list: unique solutions
		aset = new HashSet<>(slist);
		loader.writeWords(new ArrayList<>(aset), outPastSolutionsFile.toString());
	}
}
