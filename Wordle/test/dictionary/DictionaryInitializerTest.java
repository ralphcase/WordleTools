package dictionary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import word.Word;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

public class DictionaryInitializerTest {

    @Test
    void loadsAllowedAndGoalsWithoutPastSolutions(@TempDir Path tempDir) throws Exception {
        // Create allowed_words.txt
        File allowed = tempDir.resolve(DictionaryInitializer.ALLOWED_WORDS_FILE).toFile();
        try (FileWriter w = new FileWriter(allowed)) {
            w.write("crane slate");
        }

        // Create goals.txt
        File goals = tempDir.resolve(DictionaryInitializer.GOAL_WORDS_FILE).toFile();
        try (FileWriter w = new FileWriter(goals)) {
            w.write("crane");
        }

        // Do NOT create past_solutions.txt

        DictionaryInitializer init = new DictionaryInitializer(tempDir.toFile());
        WordRepository repo = init.loadDictionaries();

        Assertions.assertEquals(2, repo.allowedWords().size());
        Assertions.assertEquals(1, repo.goalWords().size());
        Assertions.assertTrue(repo.pastSolutionWords().isEmpty());
    }

    @Test
    void loadsPastSolutionsWhenPresent(@TempDir Path tempDir) throws Exception {
        // Create allowed_words.txt
        File allowed = tempDir.resolve(DictionaryInitializer.ALLOWED_WORDS_FILE).toFile();
        try (FileWriter w = new FileWriter(allowed)) {
            w.write("crane slate rebus");
        }

        // Create goals.txt
        WordRepository repo = getWordRepository(tempDir);

        Assertions.assertEquals(3, repo.allowedWords().size());
        Assertions.assertEquals(2, repo.goalWords().size());
        Assertions.assertEquals(1, repo.pastSolutionWords().size());
        Assertions.assertEquals(new Word("CRANE"), repo.pastSolutionWords().getFirst());
    }

    private static WordRepository getWordRepository(Path tempDir) throws IOException {
        File goals = tempDir.resolve(DictionaryInitializer.GOAL_WORDS_FILE).toFile();
        try (FileWriter w = new FileWriter(goals)) {
            w.write("crane slate");
        }

        // Create past_solutions.txt
        File past = tempDir.resolve(DictionaryInitializer.PAST_SOLUTIONS_FILE).toFile();
        try (FileWriter w = new FileWriter(past)) {
            w.write("crane");
        }

        DictionaryInitializer init = new DictionaryInitializer(tempDir.toFile());
        return init.loadDictionaries();
    }

    @Test
    void throwsIfAllowedWordsMissing(@TempDir Path tempDir) {
        // Create goals.txt only
        File goals = tempDir.resolve(DictionaryInitializer.GOAL_WORDS_FILE).toFile();
        try (FileWriter w = new FileWriter(goals)) {
            w.write("crane");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DictionaryInitializer init = new DictionaryInitializer(tempDir.toFile());

        Assertions.assertThrows(RuntimeException.class, init::loadDictionaries);
    }

    @Test
    void throwsIfGoalWordsMissing(@TempDir Path tempDir) {
        // Create allowed_words.txt only
        File allowed = tempDir.resolve(DictionaryInitializer.ALLOWED_WORDS_FILE).toFile();
        try (FileWriter w = new FileWriter(allowed)) {
            w.write("crane slate");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DictionaryInitializer init = new DictionaryInitializer(tempDir.toFile());

        Assertions.assertThrows(RuntimeException.class, init::loadDictionaries);
    }
}
