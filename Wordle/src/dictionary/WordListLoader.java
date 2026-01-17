package dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import position.Position;

public final class WordListLoader {

    /**
     * Load a list of Position objects from a text file.
     * The file may contain words separated by whitespace, punctuation, or newlines.
     */
    public List<Position> loadPositions(String filename) {
        Set<Position> result = new HashSet<>();
        StringBuilder buffer = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            int ch;
            while ((ch = reader.read()) != -1) {
                char letter = Character.toUpperCase((char) ch);

                if (Position.LETTERS.indexOf(letter) >= 0) {
                    buffer.append(letter);
                } else {
                    flushWord(buffer, result);
                }
            }
            flushWord(buffer, result); // flush last word if needed
        } catch (IOException e) {
            throw new RuntimeException("Error reading dictionary file: " + filename, e);
        }

        return new ArrayList<>(result);
    }

    /**
     * Write a list of words (Strings) to a file.
     * Words are written space-separated with occasional newlines for readability.
     */
    public void writeWords(List<String> words, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            int count = 0;
            for (String word : words) {
                writer.write(word);
                writer.write(' ');
                count++;
                if (count % 22 == 0) {
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing dictionary file: " + filename, e);
        }
    }

    /**
     * Helper: convert buffered characters into a Position if valid.
     */
    private void flushWord(StringBuilder buffer, Set<Position> result) {
        if (buffer.length() == Position.NUMBERCELLS) {
            try {
                result.add(new Position(buffer.toString()));
            } catch (IllegalArgumentException ignored) {
                // invalid word, skip
            }
        }
        buffer.setLength(0);
    }
}