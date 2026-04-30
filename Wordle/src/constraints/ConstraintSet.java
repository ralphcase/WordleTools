package constraints;

import feedback.Feedback;
import feedback.Mark;
import word.Word;

public final class ConstraintSet {

	private final Character[] mustBe = new Character[5];

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
		            throw new IllegalStateException(
		                "Conflicting green constraint at position " + i
		            );
		        }

		        mustBe[i] = letter;
		    }
		}
		return this;
	}
}
