package constraints;

import feedback.Feedback;
import feedback.Mark;
import word.Word;

public final class Constraint {

	private final Word guess;
	private final Feedback feedback;

	public Constraint(Word guess, Feedback feedback) {
		if (guess == null)
			throw new IllegalArgumentException("Guess cannot be null");
		if (feedback == null)
			throw new IllegalArgumentException("Feedback cannot be null");
		this.guess = guess;
		this.feedback = feedback;
	}

	public boolean allows(Word candidate) {
		char[] guessLetters = guess.letters();
		char[] candidateLetters = candidate.letters();
		Mark[] marks = feedback.marks();

		// ------------------------------------------------------------
		// 1. Positional rules (CORRECT, PRESENT, ABSENT)
		// ------------------------------------------------------------
		for (int i = 0; i < Word.LENGTH; i++) {
			char letter = guessLetters[i];
			switch (marks[i]) {

				case CORRECT -> {
					if (candidateLetters[i] != letter)
						return false;
				}

				case PRESENT -> {
					if (candidateLetters[i] == letter)
						return false; // cannot be in same position
					if (!candidate.contains(letter))
						return false;
				}

				case ABSENT -> {
					boolean candidateHasLetter = candidate.contains(letter);
					boolean guessHasPositive = feedback.hasAnyPresentOrCorrect(guess, letter);

					if (!candidateHasLetter)
						break; // candidate avoids the letter entirely - OK

					if (!guessHasPositive)
						return false; // pure gray - letter must not appear anywhere

					// mixed marks - ABSENT forbids only this position
					if (candidateLetters[i] == letter)
						return false;
				}
			}
		}

		// ------------------------------------------------------------
		// 2. Duplicate-letter rules (minCount / maxCount per letter)
		// ------------------------------------------------------------
		// For each letter A-Z, compute:
		// - how many times it appears in the guess
		// - how many of those positions are PRESENT or CORRECT
		// - how many are ABSENT
		// Then enforce Wordle's per-letter multiplicity rules.
		// ------------------------------------------------------------

		for (char letter = 'A'; letter <= 'Z'; letter++) {
			int guessCount = 0;
			int positiveCount = 0; // PRESENT or CORRECT
			int absentCount = 0;

			for (int i = 0; i < Word.LENGTH; i++) {
				if (guessLetters[i] == letter) {
					guessCount++;
					if (marks[i] == Mark.CORRECT || marks[i] == Mark.PRESENT) {
						positiveCount++;
					} else {
						absentCount++;
					}
				}
			}

			if (guessCount == 0)
				continue; // letter not in guess - no constraints

			int candidateCount = candidate.count(letter);

			// Case 1: All marks for this letter are ABSENT - letter must not appear
			if (positiveCount == 0) {
				if (candidateCount > 0)
					return false;
				continue;
			}

			// Case 2: Mixed marks (some PRESENT/CORRECT, some ABSENT)
			// Wordle rule: candidate must contain EXACTLY positiveCount copies
			if (absentCount > 0) {
				if (candidateCount != positiveCount)
					return false;
				continue;
			}

			// Case 3: All marks for this letter are PRESENT or CORRECT
			// Wordle rule: candidate must contain AT LEAST positiveCount copies
			if (candidateCount < positiveCount)
				return false;
		}

		return true;
	}

	public String toString() {
		return guess + "\t" + feedback;
	}
}
