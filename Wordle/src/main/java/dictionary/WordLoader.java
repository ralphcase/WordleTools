package dictionary;

import word.Word;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class WordLoader {

    private static final int WORDSPERROW = 22;

	/**
     * Load a list of Word objects from a text file.
     * The file may contain words separated by whitespace, punctuation, or newlines.
     * Lowercase is allowed; Word constructor enforces uppercase and validity.
     * Invalid words are skipped with a console message.
     */
    public List<Word> loadWords(Path filePath) {
        List<Word> result = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                char c = (char) ch;

                if (Character.isLetter(c)) {
                    buffer.append(c);
                } else {
                    flushWord(buffer, result);
                }
            }
            flushWord(buffer, result); // flush last token
        } catch (IOException e) {
            throw new RuntimeException("Error reading dictionary file: " + filePath, e);
        }

        System.out.println(filePath + " has "+result.size() +" words.");
        return result;
    }

    /**
     * Helper: convert buffered characters into a Word if valid.
     */
    private void flushWord(StringBuilder buffer, List<Word> result) {
        if (!buffer.isEmpty()) {
            String token = buffer.toString();
            try {
                result.add(new Word(token));
            } catch (IllegalArgumentException ex) {
                System.err.println("Skipping invalid word: \"" + token + "\" (" + ex.getMessage() + ")");
            }
        }
        buffer.setLength(0);
    }

    /**
     * Write a collection of Word objects to a file.
     * Words are written space-separated with occasional newlines for readability.
     */
    public void writeWords(Collection<Word> words, Path filename) {
        try (FileWriter writer = new FileWriter(filename.toFile())) {
            int count = 0;
            for (Word w : words) {
                writer.write(w.toString());
                writer.write(' ');
                count++;
                if (count % WORDSPERROW == 0) {
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing dictionary file: " + filename, e);
        }
    }
}
