package solver;

import word.Word;

public record GuessScore(Word word, double score) {
    public String toString() {
        return "%s: %.3f".formatted(word, score);
    }
}
