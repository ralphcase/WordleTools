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

	@Test
	void updatedByRecordsGreenLetters() {
		ConstraintSet cs = new ConstraintSet();

		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT });

		cs.updatedBy(guess, fb);

		// Now assert that position 0 must be 'A'
		assertEquals(guess.text().charAt(0), cs.mustBe()[0]);
	}

	@Test
	void updatedByRejectsConflictingGreenLetters() {
		ConstraintSet cs = new ConstraintSet();

		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT }));

		assertThrows(IllegalStateException.class, () -> cs.updatedBy(new Word("BPPLE"),
				new Feedback(new Mark[] { Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT })));
	}

	@Test
	void updatedByRecordsYellowLetters() {
		ConstraintSet cs = new ConstraintSet();

		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT });

		cs.updatedBy(guess, fb);

		assertTrue(cs.mustContain().contains('P'));
	}

	@Test
	void updatedByRejectsYellowInGreenPosition() {
		ConstraintSet cs = new ConstraintSet();

		// First establish a green constraint at position 1
		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT }));

		// Now try to say that same letter is yellow at the same position
		assertThrows(IllegalStateException.class, () -> cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT })));
	}

}
