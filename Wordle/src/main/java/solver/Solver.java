package solver;

import constraints.Constraint;
import dictionary.StarterCache;
import dictionary.WordRepository;
import feedback.Feedback;
import word.Word;

import java.util.*;

public class Solver {

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
                newSolver = new Solver(repository, hardmode, Mode.NEW);
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
//        System.out.println(constraint);
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
        System.out.println("Scoring " + allowedWords.size() + " allowed words against " + goalWords.size() + " goal words.");

        for (Word w : allowedWords) {
            double score = scoreWord(w);
            pq.add(new GuessScore(w, score));
            maxScore = Math.max(maxScore, score);
        }

        List<GuessScore> result = new ArrayList<>(pq.size());
        while (!pq.isEmpty() && top > 0) {
            top--;
            GuessScore g = pq.poll();      // poll returns lowest score first
            result.add(new GuessScore(g.word(), g.score() / maxScore));
        }
        for (int i = 0; i < Math.min(10, result.size()); i++) {
            System.out.print(result.get(i)+" ");
        }
        System.out.println(" ...");
        if (newSolver != null) {
            List<GuessScore> newSolverGuesses = newSolver.rankedGuesses(top);
//            System.out.println(newSolverGuesses);
//            System.out.println(result);
            // Create a weighted average of the scores from this solver and the newSolver.
            double newFactor = (2 * wordRepository.pastSolutionWords().size() - wordRepository.goalWords().size()) / (double) wordRepository.goalWords().size();
            HashMap<Word, Double> accum = new HashMap<>();
            for  (GuessScore g : result) {
                accum.put(g.word(), 1 / g.score());
            }
            for  (GuessScore g : newSolverGuesses) {
                accum.put(g.word(), accum.get(g.word()) + newFactor / g.score());
            }
            pq = new PriorityQueue<>(Comparator.comparingDouble(GuessScore::score));

            maxScore = 0;
            for (Map.Entry<Word, Double> entry : accum.entrySet()) {
                pq.add(new GuessScore(entry.getKey(), 1 / entry.getValue()));
                maxScore = Math.max(maxScore, 1 / entry.getValue());
            }

            result = new ArrayList<>(pq.size());
            while (!pq.isEmpty() && top > 0) {
                top--;
                GuessScore g = pq.poll();      // poll returns lowest score first
                result.add(new GuessScore(g.word(), g.score() / maxScore));
            }
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
        if (this.scope == Mode.NEW) {
            if (!goalWords.contains(w)) {
                score *= (double) (constraints + 2) / (constraints + 1);
            }
        }
        return score;
    }

    public enum Mode {
        ARCHIVE, NEW, ALL, SMART
    }
}