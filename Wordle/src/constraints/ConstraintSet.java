package constraints;

import java.util.Arrays;

import feedback.Feedback;
import feedback.Mark;
import word.Word;

public final class ConstraintSet {

	private final Character[] mustBe = new Character[5];
	private final java.util.Set<Character> mustContain = new java.util.HashSet<>();
	private final java.util.Set<Character> cannotContain = new java.util.HashSet<>();


	public ConstraintSet() {
		// empty initial constraints
	}

	public boolean allows(Object candidate) {
		// placeholder implementation
		return true;
	}

	public ConstraintSet updatedBy(Word guess, Feedback fb) {
		if (guess.text().length() != Feedback.LENGTH) {
			throw new IllegalArgumentException("Guess and feedback must have the same length");
		}

		for (int i = 0; i < Feedback.LENGTH; i++) {
			if (fb.marks()[i] == Mark.CORRECT) {
				char letter = guess.text().charAt(i);

				if (mustBe[i] != null && mustBe[i] != letter) {
					throw new IllegalStateException("Conflicting green constraint at position " + i);
				}

				mustBe[i] = letter;
			}

			if (fb.marks()[i] == Mark.PRESENT) {
				char letter = guess.text().charAt(i);

				// Rule 1: the letter must appear somewhere
				mustContain.add(letter);

				// Rule 2: the letter cannot be in this position
				if (mustBe[i] != null && mustBe[i] == letter) {
					throw new IllegalStateException("Yellow letter " + letter + " cannot be at position " + i);
				}
			}
			
			if (fb.marks()[i] == Mark.ABSENT) {
			    char letter = guess.text().charAt(i);

			    // If we already know the letter must appear somewhere (from yellow),
			    // we cannot forbid it globally yet — skip for now.
			    if (!mustContain.contains(letter)) {
			        cannotContain.add(letter);
			    }
			}


		}

		return this;
	}

	public Character[] mustBe() {
		return Arrays.copyOf(mustBe, mustBe.length);
	}

	public java.util.Set<Character> mustContain() {
		return new java.util.HashSet<>(mustContain);
	}
	public java.util.Set<Character> cannotContain() {
	    return new java.util.HashSet<>(cannotContain);
	}

}
