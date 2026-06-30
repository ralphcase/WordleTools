package solver;

import word.Word;

/**
 * Package up a Word and its score in the current environment.
 * @param word  The Word
 * @param score The score - lower is better.
 */
public record GuessScore(Word word, double score) {
    public String toString() {
        return "%s: %.3f".formatted(word, score);
    }
}
