package dictionary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import position.Position;

public final class WordRepository {

    private final List<Position> goalWords;
    private final List<Position> allowedWords;
    private final List<Position> solutionWords;

    /**
     * Construct a repository from lists loaded by WordListLoader.
     */
    public WordRepository(
            List<Position> goalWords,
            List<Position> allowedWords,
            List<Position> solutionWords) {

        this.goalWords = new ArrayList<>(goalWords);
        this.allowedWords = new ArrayList<>(allowedWords);
        this.solutionWords = new ArrayList<>(solutionWords);
    }

    /**
     * Words that can be chosen as the hidden Wordle solution.
     */
    public List<Position> getGoalWords() {
        return Collections.unmodifiableList(goalWords);
    }

    /**
     * Words that are allowed as guesses (may include goal words).
     */
    public List<Position> getAllowedWords() {
        return Collections.unmodifiableList(allowedWords);
    }

    /**
     * Words that your solver has identified as valid solutions
     * (your old SOLUTIONWORDS list).
     */
    public List<Position> getSolutionWords() {
        return Collections.unmodifiableList(solutionWords);
    }

    /**
     * ALLWORDS = allowedWords ∪ goalWords
     * This matches your old behavior.
     */
    public List<Position> getAllWords() {
        Set<Position> merged = new HashSet<>(allowedWords);
        merged.addAll(goalWords);
        return new ArrayList<>(merged);
    }
}