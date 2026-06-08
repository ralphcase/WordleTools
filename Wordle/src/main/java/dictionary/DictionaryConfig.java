package dictionary;

import java.nio.file.Path;

public record DictionaryConfig(
        Path baseDir,
        String allowedWordsFile,
        String goalWordsFile,
        String pastSolutionsFile,
        String solutionsFile,
        String archiveFile,
        String starterCacheFile,
        String wordleBotFile
) {

    public static DictionaryConfig testConfig(Path baseDir) {
        return new DictionaryConfig(
                baseDir,
                "allowed_words.txt",
                "goals.txt",
                "past_solutions.txt",
                "solutions.txt",
                "archive_solutions.txt",
                "starter-cache.json",
                "wordlebot.txt"
        );
    }

    public static DictionaryConfig defaultConfig() {
        return new DictionaryConfig(
                Path.of("."),
                "allowed_words.txt",
                "goals.txt",
                "past_solutions.txt",
                "solutions.txt",
                "archive_solutions.txt",
                "starter-cache.json",
                "wordlebot.txt"
        );
    }

    public Path allowedWordsPath() {
        return baseDir.resolve(allowedWordsFile);
    }

    public Path goalWordsPath() {
        return baseDir.resolve(goalWordsFile);
    }

    public Path pastSolutionsPath() {
        return baseDir.resolve(pastSolutionsFile);
    }

    public Path starterCachePath() {
        return baseDir.resolve(starterCacheFile);
    }

    public Path wordlebotPath() {
        return baseDir.resolve(wordleBotFile);
    }

    public Path solutionsWordsPath() { return baseDir.resolve(solutionsFile); }

    public Path archiveWordsPath() { return baseDir.resolve(archiveFile); }
}
