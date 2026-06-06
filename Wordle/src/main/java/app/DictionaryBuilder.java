package app;

import dictionary.DictionaryConfig;
import dictionary.WordLoader;
import word.Word;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DictionaryBuilder {

	private static final String GOALFILE = "goals.txt";
	private static final String ALLOWEDFILE = "allowed_words.txt";
	private static final String SOLUTIONFILE = "solutions.txt";
	private static final String WORDLEBOT = "wordlebot.txt";
	private static final String ARCHIVE = "archive_solutions.txt";
	private static final String PASTSOLUTIONS = "past_solutions.txt";

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

		rebuildDictionaries(DictionaryConfig.defaultConfig());

		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
	}

	/**
	 * Package-private so tests in the same package can call it.
	 * This method performs all the real work. main() is now just a wrapper.
	 */
	static void rebuildDictionaries(DictionaryConfig config) {

		WordLoader loader = new WordLoader();

		// Build new goals list: union of goals + wordlebot
		Set<Word> aset = new HashSet<>(loader.loadWords(config.goalWordsPath()));
		aset.addAll(loader.loadWords(config.wordlebotPath()));
		loader.writeWords(new ArrayList<>(aset), config.goalWordsPath());

		// Build past solutions list: unique solutions
		aset = new HashSet<>(loader.loadWords(config.solutionsWordsPath()));
		loader.writeWords(new ArrayList<>(aset), config.pastSolutionsPath());

	}
}
