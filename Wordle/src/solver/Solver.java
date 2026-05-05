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
	private final List<Word> goalWords;

	public Solver(WordRepository repository) {
		if (repository == null) {
			throw new IllegalArgumentException("Repository cannot be null");
		}
		this.repository = repository;
		this.goalWords = repository.getGoalWords();
	}

	/**
	 * Returns all candidate words that satisfy every constraint.
	 */
	public List<Word> remainingCandidates() {
		return goalWords.stream().filter(word -> constraints.stream().allMatch(c -> c.allows(word))).toList();
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
	public Word nextGuessSimple() {
		List<Word> candidates = remainingCandidates();
		return candidates.isEmpty() ? null : candidates.get(0);
	}

	public Word nextGuess() {
		List<Word> allowed = repository.getAllowedWords();
//		List<Word> allowed = repository.getGoalWords();
//		List<Word> allowed = new ArrayList<Word>();
//		allowed.add(new Word("RISER"));
		Word result = allowed.get(0);
		List<Word> goals = remainingCandidates();
		int minTotal = Integer.MAX_VALUE;
		for (Word poss : allowed) {
			int total = 0;
			for (Word target : goals) {
				if (!target.equals(poss)) {
					Constraint c = new Constraint(poss, Feedback.from(poss, target));
					constraints.add(c);
					List<Word> r = remainingCandidates();
					constraints.remove(c);
					total += r.size();
				}
			}
			System.out.print(".");
			if (total < minTotal) {
				System.out.println();
				System.out.println(poss + " (" + total + ")");
				minTotal = total;
				result = poss;
			}
		}
		System.out.println();
		return result;
	}
}
