package constraints;

import org.junit.jupiter.api.Test;

import feedback.Feedback;
import feedback.Mark;
import word.Word;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

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
	}

	@Test
	void updatedByRecordsGreenLetters() {
		ConstraintSet cs = new ConstraintSet();

		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { Mark.CORRECT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT });

		cs.updatedBy(guess, fb);

		// Now assert that position 0 must be 'A'
		assertEquals(guess.text().charAt(0), cs.mustBe[0]);
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

		assertTrue(cs.mustContain.contains('P'));
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

	@Test
	void updatedByRecordsGrayLetters() {
		ConstraintSet cs = new ConstraintSet();

		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT });

		cs.updatedBy(guess, fb);

		assertTrue(cs.cannotContain.contains('A'));
		assertTrue(cs.cannotContain.contains('P'));
		assertTrue(cs.cannotContain.contains('L'));
		assertTrue(cs.cannotContain.contains('E'));
	}

	@Test
	void grayDoesNotOverrideYellow() {
		ConstraintSet cs = new ConstraintSet();

		// First: yellow P
		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT }));

		// P must NOT be in cannotContain because it's known to be present
		assertThrows(IllegalStateException.class, () -> cs.updatedBy(new Word("PUPPY"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT })));
	}

	@Test
	void grayForbidsLetterAtPosition() {
		ConstraintSet cs = new ConstraintSet();

		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT }));
		Set<Character> expected = new HashSet<Character>();
		expected.add('A');
		expected.add('P');
		expected.add('L');
		expected.add('E');

		assertEquals(expected, cs.cannotBe[0]);
		assertEquals(expected, cs.cannotBe[1]);
	}

	@Test
	void grayDoesNotOverrideYellowButStillForbidsPosition() {
		ConstraintSet cs = new ConstraintSet();

		// First: yellow P at position 1
		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT }));

		assertThrows(IllegalStateException.class, () ->
		// Second: gray P at position 3
		cs.updatedBy(new Word("PUPPY"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT })));

		// P must NOT be globally forbidden
		assertFalse(cs.cannotContain.contains('P'));

		// But P must be forbidden at position 3
		Set<Character> expected = new HashSet<Character>();
		expected.add('A');
		expected.add('P');
		expected.add('L');
		expected.add('E');
		expected.add('U');
		expected.add('Y');
		assertEquals(expected, cs.cannotBe[3]);
	}

	@Test
	void conflictingGrayConstraintsThrow() {
		ConstraintSet cs = new ConstraintSet();

		// First forbid 'A' at position 0
		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT }));

		// Now try to forbid a different letter at the same position
		assertThrows(IllegalStateException.class, () -> cs.updatedBy(new Word("BAPLE"),
				new Feedback(new Mark[] { Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT })));
	}

}
