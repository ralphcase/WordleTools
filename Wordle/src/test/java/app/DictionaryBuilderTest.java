package app;

import dictionary.DictionaryConfig;
import dictionary.WordLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import word.Word;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryBuilderTest {

    @Test
    void rebuildDictionaries_addsWordlebotWordsToGoals(@TempDir Path tempDir) throws Exception {
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);
        WordLoader loader = new WordLoader();

        // goals.txt
        Files.writeString(config.goalWordsPath(), "crane\nslate\n");

        // wordlebot.txt
        Files.writeString(config.wordlebotPath(), "trace\nslate\n");

        // solutions.txt (required but irrelevant for this test)
        Files.writeString(config.solutionsWordsPath(), "crane\n");

        DictionaryBuilder.rebuildDictionaries(config);

        List<Word> goals = loader.loadWords(config.goalWordsPath());
        assertEquals(3, goals.size());
        assertTrue(goals.contains(new Word("crane")));
        assertTrue(goals.contains(new Word("slate")));
        assertTrue(goals.contains(new Word("trace")));
    }

    @Test
    void rebuildDictionaries_dedupesGoals(@TempDir Path tempDir) throws Exception {
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);
        WordLoader loader = new WordLoader();

        Files.writeString(config.goalWordsPath(), "crane\ncrane\n");
        Files.writeString(config.wordlebotPath(), "crane\n");

        Files.writeString(config.solutionsWordsPath(), "dummy\n");

        DictionaryBuilder.rebuildDictionaries(config);

        List<Word> goals = loader.loadWords(config.goalWordsPath());
        assertEquals(1, goals.size());
        assertEquals("CRANE", goals.getFirst().toString());
    }

    @Test
    void rebuildDictionaries_createsPastSolutionsFromSolutions(@TempDir Path tempDir) throws Exception {
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);
        WordLoader loader = new WordLoader();

        Files.writeString(config.goalWordsPath(), "crane\n");
        Files.writeString(config.wordlebotPath(), "trace\n");

        // solutions.txt
        Files.writeString(config.solutionsWordsPath(), "alpha\nbeta\nalpha\n");

        // DO NOT create past_solutions.txt

        DictionaryBuilder.rebuildDictionaries(config);

        List<Word> past = loader.loadWords(config.pastSolutionsPath());
        assertEquals(1, past.size());
        assertTrue(past.contains(new Word("alpha")));
    }

    @Test
    void rebuildDictionaries_overwritesPastSolutions(@TempDir Path tempDir) throws Exception {
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);
        WordLoader loader = new WordLoader();

        Files.writeString(config.goalWordsPath(), "crane\n");
        Files.writeString(config.wordlebotPath(), "trace\n");

        // solutions.txt
        Files.writeString(config.solutionsWordsPath(), "alpha\nbetha\n");

        // existing past_solutions.txt (should be overwritten)
        Files.writeString(config.pastSolutionsPath(), "oldword\n");

        DictionaryBuilder.rebuildDictionaries(config);

        List<Word> past = loader.loadWords(config.pastSolutionsPath());
        assertEquals(2, past.size());
        assertTrue(past.contains(new Word("alpha")));
        assertTrue(past.contains(new Word("betha")));
    }

    @Test
    void rebuildDictionaries_handlesEmptyWordlebotFile(@TempDir Path tempDir) throws Exception {
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);
        WordLoader loader = new WordLoader();

        Files.writeString(config.goalWordsPath(), "crane\nslate\n");
        Files.writeString(config.wordlebotPath(), ""); // empty

        Files.writeString(config.solutionsWordsPath(), "dummy\n");

        DictionaryBuilder.rebuildDictionaries(config);

        List<Word> goals = loader.loadWords(config.goalWordsPath());
        assertEquals(2, goals.size());
    }
}
