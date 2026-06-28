package dictionary;

import java.nio.file.Path;

/*
Configuration to map dictionaries to their backing files.
 */
public record DictionaryConfig(
        Path baseDir,
        String allowedWordsFile,
        String goalWordsFile,
        String pastSolutionsFile,
        String solutionsFile,
        String archiveFile,
        String starterCacheFile,
        String wordleBotFile,
        String predictFie
) {
    // File name constants
    private static final String ALLOWED_WORDS = "allowed_words.txt";
    private static final String GOAL_WORDS = "goals.txt";
    private static final String PAST_SOLUTIONS = "past_solutions.txt";
    private static final String SOLUTIONS = "solutions.txt";
    private static final String ARCHIVE_SOLUTIONS = "archive_solutions.txt";
    private static final String STARTER_CACHE = "starter-cache.json";
    private static final String WORDLEBOT = "wordlebot.txt";
    private static final String PREDICT = "predicted.txt";

    public static DictionaryConfig testConfig(Path baseDir) {
        return new DictionaryConfig(
                baseDir,
                ALLOWED_WORDS,
                GOAL_WORDS,
                PAST_SOLUTIONS,
                SOLUTIONS,
                ARCHIVE_SOLUTIONS,
                STARTER_CACHE,
                WORDLEBOT,
                PREDICT
        );
    }

    public static DictionaryConfig defaultConfig() {
        return new DictionaryConfig(
                Path.of("."),
                ALLOWED_WORDS,
                GOAL_WORDS,
                PAST_SOLUTIONS,
                SOLUTIONS,
                ARCHIVE_SOLUTIONS,
                STARTER_CACHE,
                WORDLEBOT,
                PREDICT
        );
    }

    public Path allowedWordsPath() { return baseDir.resolve(allowedWordsFile); }

    public Path goalWordsPath() { return baseDir.resolve(goalWordsFile); }

    public Path pastSolutionsPath() { return baseDir.resolve(pastSolutionsFile); }

    public Path starterCachePath() { return baseDir.resolve(starterCacheFile); }

    public Path wordlebotPath() { return baseDir.resolve(wordleBotFile); }

    public Path solutionsWordsPath() { return baseDir.resolve(solutionsFile); }

    public Path archiveWordsPath() { return baseDir.resolve(archiveFile); }

    public Path predictedPath() { return baseDir.resolve(predictFie); }
}
