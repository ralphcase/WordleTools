package dictionary;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import word.Word;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DictionaryHashTest {
    @Test
    void sameFilesProduceSameHash() throws IOException {
        Path allowed = tempFile("apple\nberry\nchase\n");
        Path solutions = tempFile("apple\nberry\n");

        String h1 = DictionaryHash.compute(allowed, solutions);
        String h2 = DictionaryHash.compute(allowed, solutions);

        assertEquals(h1, h2);
    }
    @Test
    void changingAllowedWordsChangesHash() throws IOException {
        Path allowed1 = tempFile("apple\nberry\nchase\n");
        Path allowed2 = tempFile("apple\nberry\nchase\nzebra\n");
        Path solutions = tempFile("apple\nberry\n");

        String h1 = DictionaryHash.compute(allowed1, solutions);
        String h2 = DictionaryHash.compute(allowed2, solutions);

        assertNotEquals(h1, h2);
    }
    @Test
    void changingSolutionsChangesHash() throws IOException {
        Path allowed = tempFile("apple\nberry\nchase\n");
        Path solutions1 = tempFile("apple\nberry\n");
        Path solutions2 = tempFile("apple\nberry\nchase\n");

        String h1 = DictionaryHash.compute(allowed, solutions1);
        String h2 = DictionaryHash.compute(allowed, solutions2);

        assertNotEquals(h1, h2);
    }
    @Test
    void orderChangesChangeHash() throws IOException {
        Path allowed1 = tempFile("apple\nberry\nchase\n");
        Path allowed2 = tempFile("chase\nberry\napple\n");
        Path solutions = tempFile("apple\nberry\n");

        String h1 = DictionaryHash.compute(allowed1, solutions);
        String h2 = DictionaryHash.compute(allowed2, solutions);

        assertNotEquals(h1, h2);
    }
    private Path tempFile(String contents) throws IOException {
        Path p = Files.createTempFile("dict", ".txt");
        Files.writeString(p, contents, StandardCharsets.UTF_8);
        return p;
    }

}
