package app;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import word.Word;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DictionaryBuilderTest {

    @TempDir
    Path tempDir;

    /**
     * Helper: read output files exactly as written by WordLoader.writeWords().
     * Splits on whitespace, converts tokens to Word objects.
     */
    private List<Word> readWords(Path file) throws IOException {
        return Files.readAllLines(file).stream()
                .flatMap(line -> Stream.of(line.split("\\s+")))
                .filter(s -> !s.isBlank())
                .map(Word::new)
                .toList();
    }

    @Test
    void rebuildDictionaries_writesExpectedOutputs() throws IOException {
        // Arrange input files
        Path goals = tempDir.resolve("goals.txt");
        Path allowed = tempDir.resolve("allowed_words.txt");
        Path solutions = tempDir.resolve("solutions.txt");
        Path wordlebot = tempDir.resolve("wordlebot.txt");

        Files.write(goals, List.of("crane", "slate"));
        Files.write(allowed, List.of("trace"));
        Files.write(solutions, List.of("crane"));
        Files.write(wordlebot, List.of("slate"));

        // Output files
        Path outGoals = tempDir.resolve("goals_out.txt");
        Path outPastSolutions = tempDir.resolve("past_solutions_out.txt");

        // Act
        DictionaryBuilder.rebuildDictionaries(
                goals,
                allowed,
                solutions,
                wordlebot,
                outGoals,
                outPastSolutions
        );

        // Assert: output files exist
        assertTrue(Files.exists(outGoals));
        assertTrue(Files.exists(outPastSolutions));

        // Assert: goals_out = union(goals, wordlebot)
        List<Word> goalsOut = readWords(outGoals);
        assertEquals(2, goalsOut.size());
        assertTrue(goalsOut.contains(new Word("crane")));
        assertTrue(goalsOut.contains(new Word("slate")));

        // Assert: past_solutions_out = unique(solutions)
        List<Word> pastSolutionsOut = readWords(outPastSolutions);
        assertEquals(List.of(new Word("crane")), pastSolutionsOut);
    }

    @Test
    void rebuildDictionaries_handlesEmptyInputFile() throws IOException {
        Path goals = tempDir.resolve("goals.txt");
        Path allowed = tempDir.resolve("allowed_words.txt");
        Path solutions = tempDir.resolve("solutions.txt");
        Path wordlebot = tempDir.resolve("wordlebot.txt");

        // Empty goals file
        Files.write(goals, List.of());
        Files.write(allowed, List.of("trace"));
        Files.write(solutions, List.of("crane"));
        Files.write(wordlebot, List.of("slate"));

        Path outGoals = tempDir.resolve("goals_out.txt");
        Path outPastSolutions = tempDir.resolve("past_solutions_out.txt");

        DictionaryBuilder.rebuildDictionaries(
                goals,
                allowed,
                solutions,
                wordlebot,
                outGoals,
                outPastSolutions
        );

        // goals_out should contain only wordlebot words
        List<Word> goalsOut = readWords(outGoals);
        assertEquals(List.of(new Word("slate")), goalsOut);
    }

    @Test
    void rebuildDictionaries_throwsOnMissingInputFile() {
        Path missing = tempDir.resolve("does_not_exist.txt");

        Path dummy = tempDir.resolve("dummy.txt");
        try {
            Files.write(dummy, List.of("crane"));
        } catch (IOException e) {
            fail("Unexpected IO error in test setup");
        }

        assertThrows(RuntimeException.class, () ->
                DictionaryBuilder.rebuildDictionaries(
                        missing,
                        dummy,
                        dummy,
                        dummy,
                        tempDir.resolve("out1.txt"),
                        tempDir.resolve("out2.txt")
                )
        );
    }

    @Test
    void rebuildDictionaries_ignoresInvalidWords() throws IOException {
        Path goals = tempDir.resolve("goals.txt");
        Path allowed = tempDir.resolve("allowed_words.txt");
        Path solutions = tempDir.resolve("solutions.txt");
        Path wordlebot = tempDir.resolve("wordlebot.txt");

        // WordLoader will ignore "sl@te"
        Files.write(goals, List.of("crane", "sl@te", "trace"));
        Files.write(allowed, List.of("trace"));
        Files.write(solutions, List.of("crane"));
        Files.write(wordlebot, List.of("slate"));

        Path outGoals = tempDir.resolve("goals_out.txt");
        Path outPastSolutions = tempDir.resolve("past_solutions_out.txt");

        DictionaryBuilder.rebuildDictionaries(
                goals,
                allowed,
                solutions,
                wordlebot,
                outGoals,
                outPastSolutions
        );

        List<Word> goalsOut = readWords(outGoals);

        // "sl@te" should not appear
        assertFalse(goalsOut.contains(new Word("sl@te")));

        // Valid words should appear
        assertTrue(goalsOut.contains(new Word("crane")));
        assertTrue(goalsOut.contains(new Word("trace")));
        assertTrue(goalsOut.contains(new Word("slate")));
    }
}
