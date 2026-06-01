package solver;

import constraints.Constraint;
import dictionary.WordRepository;
import feedback.Feedback;
import word.Word;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class Solver {

    private final boolean hardmode;
    private List<Word> goalWords;
    private List<Word> allowedWords;

    private int constraints;

    public Solver(WordRepository repository) {
        this(repository, false, Mode.ALL);
    }

    public Solver(WordRepository repository, boolean hardmode, Mode archive) {
        this.hardmode = hardmode;

        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        switch (archive) {
            case ARCHIVE -> this.goalWords = repository.pastSolutionWords();
            case NEW -> {
                this.goalWords = new ArrayList<>(repository.goalWords());
                this.goalWords.removeAll(repository.pastSolutionWords());
            }
            case ALL -> this.goalWords = repository.goalWords();
            case SMART ->
                    throw new UnsupportedOperationException("SMART selection of next word is not yet implemented.");
        }

        this.allowedWords = repository.allowedWords();

        constraints = 0;
    }

    /**
     * Returns all candidate words that satisfy every constraint.
     */
    public List<Word> remainingCandidates() {
        return goalWords;
    }

    /**
     * Returns all candidate words that satisfy the given constraint. Don't update
     * the state.
     */
    public List<Word> remainingCandidates(Constraint constraint) {
        return goalWords.stream().filter(constraint::allows).toList();
    }

    /**
     * Adds new constraints derived from feedback.
     */
    public void applyFeedback(Word guess, Feedback feedback) {
        Constraint constraint = new Constraint(guess, feedback);
        constraints++;
        System.out.println(constraint);
        goalWords = goalWords.stream().filter(constraint::allows).toList();
        if (hardmode) {
            System.out.print(allowedWords.size() + " allowed words filtered to ");
            allowedWords = allowedWords.stream().filter(constraint::allows).toList();
            System.out.println(allowedWords.size() + ".");
        }
    }

    /**
     * Chooses the next guess. Just pick one.
     */
    public Word nextGuessSimple() {
        List<Word> candidates = remainingCandidates();
        return candidates.isEmpty() ? null : candidates.getFirst();
    }

    /**
     * Chooses the next guess using an estimate for which word will eliminate the
     * most goal words, on average.
     */
    public Word nextGuess() {
        return rankedGuesses(1).get(0).word();
    }

    public List<GuessScore> rankedGuesses(int top) {
        PriorityQueue<GuessScore> pq =
                new PriorityQueue<>(Comparator.comparingDouble(GuessScore::score));

        for (Word w : allowedWords) {
            pq.add(new GuessScore(w, scoreWord(w)));
        }

        List<GuessScore> result = new ArrayList<>(pq.size());
        while (!pq.isEmpty() && top > 0) {
            top--;
            result.add(pq.poll());   // poll returns lowest score first
        }

        return result;
    }

    private double scoreWord(Word w) {
        double score = 0;
        for (Word target : goalWords) {
            if (!target.equals(w)) {
                // Simulate feedback for this guess against this target
                // and count how many candidates would remain after applying that constraint
                List<Word> r = remainingCandidates(new Constraint(w, Feedback.from(w, target)));
                score += r.size();
            }
        }
        // Prefer words that could be the solution over other possible guesses.
        if (!goalWords.contains(w)) {
            score *= (double) (constraints + 2) / (constraints + 1);
        }
        return score;
    }

    public enum Mode {
        ARCHIVE, NEW, ALL, SMART
    }
}