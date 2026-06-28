package app;

import dictionary.DictionaryConfig;
import dictionary.WordLoader;
import word.Word;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Check and rebuild the dictionary files.
 */
public class DictionaryBuilder {

    /**
     * Rebuild the dictionaries from data in files.
     *
     * @param args not used
     */
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

        Set<Word> goalWords = new HashSet<>(loader.loadWords(config.goalWordsPath()));
        Set<Word> wordlebotWords = new HashSet<>(loader.loadWords(config.wordlebotPath()));
        // Union of existing goals and current wordlebot words
        goalWords.addAll(wordlebotWords);

        // Remove goals that were expected (predicted) but are no longer in wordlebot
        Set<Word> predicted = new HashSet<>(loader.loadWords(config.predictedPath()));
        predicted.removeAll(wordlebotWords);
        if (!predicted.isEmpty()) {
            System.out.println("Words that are no longer goals: " + predicted);
            goalWords.removeAll(predicted);
        }
        loader.writeWords(new ArrayList<>(goalWords), config.goalWordsPath());

        // Build past solutions list: unique solutions
        goalWords = new HashSet<>(loader.loadWords(config.solutionsWordsPath()));
        loader.writeWords(new ArrayList<>(goalWords), config.pastSolutionsPath());

    }

    /**
     * Record words predicted to be returned by wordlebot.
     */
    public static void predictWordlbot(List<Word> words) {
        WordLoader loader = new WordLoader();
        loader.writeWords(words, DictionaryConfig.defaultConfig().predictedPath());
    }
}
