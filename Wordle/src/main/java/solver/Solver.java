package solver;

import constraints.Constraint;
import dictionary.StarterCache;
import dictionary.WordRepository;
import feedback.Feedback;
import word.Word;

import java.util.*;

public class Solver {

    private static final double EPSILON = 1e-9;
    private final boolean hardmode;
    private final WordRepository wordRepository;
    private final Mode scope;
    private List<Word> goalWords;
    private List<Word> allowedWords;
    private int constraints;
    private Solver newSolver;

    public Solver(WordRepository repository) {
        this(repository, false, Mode.ALL);
    }

    public Solver(WordRepository repository, boolean hardmode, Mode archive) {
        this.hardmode = hardmode;
        this.scope = archive;
        this.wordRepository = repository;

        if (repository == null) {
            throw new IllegalArgumentException("Repository cannot be null");
        }
        switch (archive) {
            case ARCHIVE -> this.goalWords = repository.archiveWords();
            case NEW -> {
                this.goalWords = new ArrayList<>(repository.goalWords());
                this.goalWords.removeAll(repository.pastSolutionWords());
            }
            case ALL -> this.goalWords = repository.goalWords();
            case SMART -> {
                this.goalWords = repository.goalWords();
                this.newSolver = new Solver(repository, hardmode, Mode.NEW);
            }
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
        goalWords = goalWords.stream().filter(constraint::allows).toList();
        if (hardmode) {
            System.out.print(allowedWords.size() + " allowed words filtered to ");
            allowedWords = allowedWords.stream().filter(constraint::allows).toList();
            System.out.println(allowedWords.size() + ".");
        }
        if (newSolver != null) {
            newSolver.applyFeedback(guess, feedback);
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
        if (constraints == 0) {

            StarterCache cache = wordRepository.getStarterCache();
            String dictHash = wordRepository.getDictionaryHash();

            Optional<GuessScore> cached = cache.load(scope.name(), dictHash);

            if (cached.isPresent()) {
                return cached.get().word();
            }

            GuessScore best = rankedGuesses(1).getFirst();

            cache.save(scope.name(), dictHash, best);

            return best.word();
        }

        return rankedGuesses(1).getFirst().word();
    }

    public List<GuessScore> rankedGuesses() {
        return rankedGuesses(allowedWords.size());
    }

    public List<GuessScore> rankedGuesses(int top) {
        PriorityQueue<GuessScore> pq = new PriorityQueue<>(Comparator.comparingDouble(GuessScore::score));

        double maxScore = 0;
        System.out.println("Scoring " + allowedWords.size() + " allowed words against " + goalWords.size() + " goal words (" + this.scope + ").");

        for (Word w : allowedWords) {
            double score = scoreWord(w);
            pq.add(new GuessScore(w, score));
            maxScore = Math.max(maxScore, score);
        }

        // Normalize the scores to be between 0 and 1, and convert the queue to a sorted list. The lowest score is the best guess.
        List<GuessScore> result = new ArrayList<>(pq.size());
        while (!pq.isEmpty()) {
            GuessScore g = pq.poll();      // poll returns lowest score first
            result.add(new GuessScore(g.word(), g.score() / maxScore));
        }
        System.out.println(result.subList(0, Math.min(Math.min(top, 25), result.size())));

        if (newSolver != null) {
            // Create a weighted average of the scores from this solver and the newSolver.

            Map<Word, Double> newScores = new HashMap<>();
            for  (GuessScore g : newSolver.rankedGuesses()) {
                newScores.put(g.word(), g.score());
            }

            double newWeight = 2 - 2 * wordRepository.pastSolutionWords().size() / (double) wordRepository.goalWords().size();
            System.out.println("Weighting new solver scores at " + newWeight + " based on " + wordRepository.pastSolutionWords().size() + " past solutions and " + wordRepository.goalWords().size() + " goal words.");
            pq = new PriorityQueue<>(Comparator.comparingDouble(GuessScore::score));
            for (GuessScore g : result) {
                double newScore = newScores.getOrDefault(g.word(), 1.0);
                double combinedScore = (g.score() * (1 - newWeight)) + (newScore * newWeight);
                pq.add(new GuessScore(g.word(), combinedScore));
            }

            result = new ArrayList<>(pq.size());
            while (!pq.isEmpty()) {
                GuessScore g = pq.poll();      // poll returns lowest score first
//                if (g.score() == 1.0) {
//                    break;  // Don't include words that are not possible given the constraints.
//                }
                result.add(new GuessScore(g.word(), g.score()));
            }
        }

        return result.subList(0, Math.min(top, result.size()));
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
        if (this.scope == Mode.NEW) {
            if (!goalWords.contains(w)) {
                score *= (double) (constraints + 2) / (constraints + 1);
            }
        }
        if (score == 0) {
            score = EPSILON; // Avoid division by zero
        }
        return score;
    }

    public enum Mode {
        ARCHIVE, NEW, ALL, SMART
    }

}