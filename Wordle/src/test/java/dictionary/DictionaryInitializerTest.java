package dictionary;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import word.Word;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class DictionaryInitializerTest {

    @Test
    void loadsAllowedAndGoalsWithoutPastSolutions(@TempDir Path tempDir) throws Exception {
        // Create allowed_words.txt
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);
        System.out.println("Allowed path = " + config.allowedWordsPath());

        File allowed = config.allowedWordsPath().toFile();
        try (FileWriter w = new FileWriter(allowed)) {
            w.write("crane slate");
        }

        // Create goals.txt
        File goals = config.goalWordsPath().toFile();
        try (FileWriter w = new FileWriter(goals)) {
            w.write("crane");
        }

        // Do NOT create past_solutions.txt

        DictionaryInitializer init = new DictionaryInitializer(config);
        WordRepository repo = init.loadDictionaries();

        assertEquals(2, repo.allowedWords().size());
        assertEquals(1, repo.goalWords().size());
        assertTrue(repo.pastSolutionWords().isEmpty());
    }

    @Test
    void loadsPastSolutionsWhenPresent(@TempDir Path tempDir) throws Exception {
        // Create allowed_words.txt
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);
        File allowed = config.allowedWordsPath().toFile();
        try (FileWriter w = new FileWriter(allowed)) {
            w.write("crane slate rebus");
        }

        // Create goals.txt
        WordRepository repo = getWordRepository(tempDir);

        assertEquals(3, repo.allowedWords().size());
        assertEquals(2, repo.goalWords().size());
        assertEquals(1, repo.pastSolutionWords().size());
        assertEquals(new Word("CRANE"), repo.pastSolutionWords().getFirst());
    }

    private static WordRepository getWordRepository(Path tempDir) throws IOException {
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);
        File goals = config.goalWordsPath().toFile();
        try (FileWriter w = new FileWriter(goals)) {
            w.write("crane slate");
        }

        // Create past_solutions.txt
        File past = config.pastSolutionsPath().toFile();
        try (FileWriter w = new FileWriter(past)) {
            w.write("crane");
        }

        DictionaryInitializer init = new DictionaryInitializer(config);
        return init.loadDictionaries();
    }

    @Test
    void throwsIfAllowedWordsMissing(@TempDir Path tempDir) {
        // Create goals.txt only
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);

        File goals = config.goalWordsPath().toFile();
        try (FileWriter w = new FileWriter(goals)) {
            w.write("crane");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DictionaryInitializer init = new DictionaryInitializer(config);

        assertThrows(RuntimeException.class, init::loadDictionaries);
    }

    @Test
    void throwsIfGoalWordsMissing(@TempDir Path tempDir) {
        // Create allowed_words.txt only
        DictionaryConfig config = DictionaryConfig.testConfig(tempDir);

        File allowed = config.allowedWordsPath().toFile();
        try (FileWriter w = new FileWriter(allowed)) {
            w.write("crane slate");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DictionaryInitializer init = new DictionaryInitializer(config);

        assertThrows(RuntimeException.class, init::loadDictionaries);
    }
    @Test
    void defaultConstructorUsesWorkingDirectory() {
        // Arrange
        DictionaryConfig config = DictionaryConfig.defaultConfig();

        File cwd = new File(".");

        DictionaryInitializer a = new DictionaryInitializer();
        DictionaryInitializer b = new DictionaryInitializer(config);

        // Act
        WordRepository repoA = a.loadDictionaries();
        WordRepository repoB = b.loadDictionaries();

        // Assert
        assertEquals(repoB.allowedWords(), repoA.allowedWords());
        assertEquals(repoB.goalWords(), repoA.goalWords());
        assertEquals(repoB.pastSolutionWords(), repoA.pastSolutionWords());
    }

}
