package solver;

import constraints.Constraint;
import dictionary.WordRepository;
import word.Word;
import feedback.Feedback;

import java.util.ArrayList;
import java.util.List;

public class Solver {

	public enum Mode {
		ARCHIVE, NEW, ALL, SMART
	}
	private final boolean hardmode;

	private List<Word> goalWords;
	private List<Word> allowedWords;

	public Solver(WordRepository repository) {
		this(repository, false, Mode.ALL);
	}

	public Solver(WordRepository repository, boolean hardmode, Mode archive) {
		this.hardmode = hardmode;

		if (repository == null) {
			throw new IllegalArgumentException("Repository cannot be null");
		}
		switch (archive) {
		case ARCHIVE -> {
			this.goalWords = repository.getPastSolutionWords();
		}
		case NEW -> {
			this.goalWords = new ArrayList<Word>(repository.getGoalWords());
			this.goalWords.removeAll(repository.getPastSolutionWords());
		}
		case ALL -> {
			this.goalWords = repository.getGoalWords();
		}
		}

		this.allowedWords = repository.getAllowedWords();

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
		return candidates.isEmpty() ? null : candidates.get(0);
	}

	/**
	 * Chooses the next guess using an estimate for which word will eliminate the
	 * most goal words, on average.
	 */
	public Word nextGuess() {
		Word result = allowedWords.get(0);
		List<Word> goals = remainingCandidates();
		int minTotal = Integer.MAX_VALUE;
		for (Word poss : allowedWords) {
			int total = 0;
			for (Word target : goals) {
				if (!target.equals(poss)) {
					List<Word> r = remainingCandidates(new Constraint(poss, Feedback.from(poss, target)));
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
