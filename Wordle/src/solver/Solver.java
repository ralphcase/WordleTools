package solver;

import constraints.Constraint;
import dictionary.WordRepository;
import word.Word;
import feedback.Feedback;

import java.util.ArrayList;
import java.util.List;

public class Solver {

	private final WordRepository repository;
	private final List<Constraint> constraints = new ArrayList<>();

	public Solver(WordRepository repository) {
		if (repository == null) {
			throw new IllegalArgumentException("Repository cannot be null");
		}
		this.repository = repository;
	}

	/**
	 * Returns all candidate words that satisfy every constraint.
	 */
    public List<Word> remainingCandidates() {
        return repository.getGoalWords().stream()
            .filter(word -> constraints.stream().allMatch(c -> c.allows(word)))
            .toList();
    }
    
	/**
	 * Adds new constraints derived from feedback.
	 */
	public void applyFeedback(Word guess, Feedback feedback) {
		constraints.add(new Constraint(guess, feedback));
	}

	/**
	 * Chooses the next guess.
	 */
    public Word nextGuess() {
        List<Word> candidates = remainingCandidates();
        return candidates.isEmpty() ? null : candidates.get(0);
    }}
