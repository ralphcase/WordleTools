package dictionary;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import word.Word;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.util.List;

public class WordLoaderTest {

    @Test
    void loadsWordsFromMessyInput(@TempDir Path tempDir) throws Exception {
        File f = tempDir.resolve("words.txt").toFile();
        try (FileWriter w = new FileWriter(f)) {
            w.write("crane SLATE...rebus\n");
            w.write("badword6\n");
            w.write("12345\n");
            w.write("sl@te\n");
        }

        WordLoader loader = new WordLoader();
        List<Word> words = loader.loadWords(Path.of(f.getAbsolutePath()));

        Assertions.assertEquals(3, words.size());
        Assertions.assertTrue(words.contains(new Word("CRANE")));
        Assertions.assertTrue(words.contains(new Word("SLATE")));
        Assertions.assertTrue(words.contains(new Word("REBUS")));
    }

    @Test
    void writerRoundTrip(@TempDir Path tempDir) {
        File f = tempDir.resolve("out.txt").toFile();

        List<Word> original = List.of(
                new Word("CRANE"),
                new Word("SLATE"),
                new Word("REBUS")
        );

        WordLoader loader = new WordLoader();
        loader.writeWords(original, Path.of(f.getAbsolutePath()));

        List<Word> loaded = loader.loadWords(Path.of(f.getAbsolutePath()));

        Assertions.assertEquals(original, loaded);
    }
}
