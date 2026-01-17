package solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import position.Position;
import report.Report;
import dictionary.WordRepository;

public final class Solver {

    private final WordRepository repo;
    private final ConstraintEngine engine;

    private List<Position> candidates;

    /**
     * Construct a solver with a repository of word lists.
     * Starts with ALLWORDS as the initial candidate set.
     */
    public Solver(WordRepository repo) {
        this.repo = repo;
        this.engine = new ConstraintEngine();
        this.candidates = new ArrayList<>(repo.getAllWords());
    }

    /**
     * Return an unmodifiable view of the current candidate list.
     */
    public List<Position> getCandidates() {
        return Collections.unmodifiableList(candidates);
    }

    /**
     * Apply a guess + report to narrow the candidate list.
     */
    public void applyFeedback(Position guess, Report report) {
        this.candidates = engine.filter(candidates, guess, report);
    }

    /**
     * Choose the next guess.
     * 
     * This is a simple baseline strategy:
     *   - If candidates are small, guess from candidates
     *   - Otherwise, guess from the full allowed list
     * 
     * You can replace this with a smarter heuristic later.
     */
    public Position chooseNextGuess() {
        if (candidates.isEmpty()) {
            throw new IllegalStateException("No candidates left — inconsistent feedback?");
        }

        if (candidates.size() <= 2) {
            return candidates.get(0);
        }

        // Simple baseline: pick the first allowed word
        return repo.getAllowedWords().get(0);
    }

    /**
     * Reset the solver to its initial state.
     */
    public void reset() {
        this.candidates = new ArrayList<>(repo.getAllWords());
    }
}