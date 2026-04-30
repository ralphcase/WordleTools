package constraints;

import org.junit.jupiter.api.Test;

import feedback.Feedback;
import feedback.Mark;
import word.Word;

import static org.junit.jupiter.api.Assertions.*;

class ConstraintSetTest {

	@Test
	void constraintSetCanBeConstructed() {
		ConstraintSet cs = new ConstraintSet();
		assertNotNull(cs);
	}

	@Test
	void allowsAlwaysReturnsTrueInitially() {
		ConstraintSet cs = new ConstraintSet();
		assertTrue(cs.allows(new Object()));
	}

	@Test
	void updatedByReturnsSameInstanceForNow() {
		ConstraintSet cs = new ConstraintSet();
		assertSame(cs, cs.updatedBy(new Word("DINGO"),
				new Feedback(new Mark[] { Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT })));
	}

	@Test
	void updatedByRejectsMismatchedLengths() {
		ConstraintSet cs = new ConstraintSet();
		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.CORRECT });

		// This should pass
		cs.updatedBy(guess, fb);

		// Now try a mismatched guess
		Word shortGuess = new Word("DOG");

		assertThrows(IllegalArgumentException.class, () -> cs.updatedBy(shortGuess, fb));
	}

}
