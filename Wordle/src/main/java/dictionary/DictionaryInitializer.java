package dictionary;

import word.Word;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public final class DictionaryInitializer {

    // -----------------------------
    // Dictionary file configuration
    // -----------------------------
    public static final String ALLOWED_WORDS_FILE = "allowed_words.txt";
    public static final String GOAL_WORDS_FILE = "goals.txt";
    //    public static final String PAST_SOLUTIONS_FILE = "solutions.txt";
    public static final String PAST_SOLUTIONS_FILE = "archive_solutions.txt";

    private final File baseDir;
    private final WordLoader loader = new WordLoader();

    public DictionaryInitializer() {
        this.baseDir = new File(".");
    }

    public DictionaryInitializer(File baseDir) {
        this.baseDir = baseDir;
    }

    public WordRepository loadDictionaries() {

        File allowedFile = new File(baseDir, ALLOWED_WORDS_FILE);
        File goalsFile = new File(baseDir, GOAL_WORDS_FILE);
        File pastFile = new File(baseDir, PAST_SOLUTIONS_FILE);

        List<Word> allowed = loader.loadWords(allowedFile.getPath());
        List<Word> goals = loader.loadWords(goalsFile.getPath());

        List<Word> pastSolutions = null;
        if (pastFile.exists()) {
            pastSolutions = loader.loadWords(pastFile.getPath());
        }

        String dictHash = DictionaryHash.compute(
                Path.of(ALLOWED_WORDS_FILE),
                Path.of(GOAL_WORDS_FILE)
        );
        StarterCache cache = new StarterCache(new File("starter-cache.json"));

        return new WordRepository(allowed, goals, pastSolutions, dictHash, cache);
    }
}
