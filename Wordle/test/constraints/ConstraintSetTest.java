package constraints;

import org.junit.jupiter.api.Test;

import feedback.Feedback;
import feedback.Mark;
import static feedback.Mark.*;
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
				new Feedback(new Mark[] { CORRECT, PRESENT, ABSENT, ABSENT, CORRECT })));
	}

	@Test
	void updatedByRejectsMismatchedLengths() {
		ConstraintSet cs = new ConstraintSet();
		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { CORRECT, PRESENT, ABSENT, ABSENT, CORRECT });

		// This should pass
		cs.updatedBy(guess, fb);
	}

	@Test
	void updatedByRecordsGreenLetters() {
		ConstraintSet cs = new ConstraintSet();

		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { CORRECT, ABSENT, ABSENT, ABSENT, ABSENT });

		cs.updatedBy(guess, fb);

		// Now assert that position 0 must be 'A'
		assertEquals(guess.text().charAt(0), cs.mustBe[0]);
	}

	@Test
	void updatedByRejectsConflictingGreenLetters() {
		ConstraintSet cs = new ConstraintSet();

		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { CORRECT, ABSENT, ABSENT, ABSENT, ABSENT }));

		assertThrows(IllegalStateException.class, () -> cs.updatedBy(new Word("BPPLE"),
				new Feedback(new Mark[] { CORRECT, ABSENT, ABSENT, ABSENT, ABSENT })));
	}

	@Test
	void updatedByRecordsYellowLetters() {
		ConstraintSet cs = new ConstraintSet();

		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { ABSENT, PRESENT, ABSENT, ABSENT, ABSENT });

		cs.updatedBy(guess, fb);

		assertTrue(cs.mustContain.contains('P'));
	}

	@Test
	void updatedByRejectsYellowInGreenPosition() {
		ConstraintSet cs = new ConstraintSet();

		// First establish a green constraint at position 1
		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { ABSENT, CORRECT, ABSENT, ABSENT, ABSENT }));

		// Now try to say that same letter is yellow at the same position
		assertThrows(IllegalStateException.class, () -> cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { ABSENT, PRESENT, ABSENT, ABSENT, ABSENT })));
	}

	@Test
	void updatedByRecordsGrayLetters() {
		ConstraintSet cs = new ConstraintSet();

		Word guess = new Word("APPLE");
		Feedback fb = new Feedback(new Mark[] { ABSENT, ABSENT, ABSENT, ABSENT, ABSENT });

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
				new Feedback(new Mark[] { ABSENT, PRESENT, ABSENT, ABSENT, ABSENT }));

		// P must NOT be in cannotContain because it's known to be present
		assertThrows(IllegalStateException.class, () -> cs.updatedBy(new Word("PUPPY"),
				new Feedback(new Mark[] { ABSENT, ABSENT, ABSENT, ABSENT, ABSENT })));
	}

	@Test
	void grayForbidsLetterAtPosition() {
		ConstraintSet cs = new ConstraintSet();

		cs.updatedBy(new Word("APPLE"),
				new Feedback(new Mark[] { ABSENT, ABSENT, ABSENT, ABSENT, ABSENT }));
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
				new Feedback(new Mark[] { ABSENT, PRESENT, ABSENT, ABSENT, ABSENT }));

		assertThrows(IllegalStateException.class, () ->
		// Second: gray P at position 3
		cs.updatedBy(new Word("PUPPY"),
				new Feedback(new Mark[] { ABSENT, ABSENT, ABSENT, ABSENT, ABSENT })));

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
				new Feedback(new Mark[] { ABSENT, ABSENT, ABSENT, ABSENT, ABSENT }));

		// Now try to forbid a different letter at the same position
		assertThrows(IllegalStateException.class, () -> cs.updatedBy(new Word("BAPLE"),
				new Feedback(new Mark[] { ABSENT, PRESENT, ABSENT, ABSENT, ABSENT })));
	}

    @Test
    void green_sets_exact_position_and_mustContain() {
        ConstraintSet cs = new ConstraintSet();

        Word guess = new Word("ABCDE");
        Feedback fb = Feedback.of(
            CORRECT, ABSENT, ABSENT, ABSENT, ABSENT
        );

        cs.updatedBy(guess, fb);

        assertEquals('A', cs.mustBe[0]);
        assertTrue(cs.mustContain.contains('A'));
        assertFalse(cs.cannotContain.contains('A'));
        assertFalse(cs.cannotBe[0].contains('A'));
    }

    @Test
    void yellow_forbids_position_and_requires_presence() {
        ConstraintSet cs = new ConstraintSet();

        Word guess = new Word("ABCDE");
        Feedback fb = Feedback.of(
            PRESENT, ABSENT, ABSENT, ABSENT, ABSENT
        );

        cs.updatedBy(guess, fb);

        assertTrue(cs.cannotBe[0].contains('A'));
        assertTrue(cs.mustContain.contains('A'));
        assertFalse(cs.cannotContain.contains('A'));
    }

    @Test
    void pure_gray_forbids_letter_globally() {
        ConstraintSet cs = new ConstraintSet();

        Word guess = new Word("abcde");
        Feedback fb = Feedback.of(
            ABSENT, ABSENT, ABSENT, ABSENT, ABSENT
        );

        cs.updatedBy(guess, fb);

        assertTrue(cs.cannotContain.contains('A'));
        assertTrue(cs.cannotContain.contains('B'));
        assertTrue(cs.cannotContain.contains('C'));
        assertTrue(cs.cannotContain.contains('D'));
        assertTrue(cs.cannotContain.contains('E'));

        for (int i = 0; i < Word.LENGTH; i++) {
            assertTrue(cs.cannotBe[i].contains('A'));
            assertTrue(cs.cannotBe[i].contains('B'));
            assertTrue(cs.cannotBe[i].contains('C'));
            assertTrue(cs.cannotBe[i].contains('D'));
            assertTrue(cs.cannotBe[i].contains('E'));
        }
    }

    @Test
    void mixed_gray_and_present_sets_exact_maxCount() {
        ConstraintSet cs = new ConstraintSet();

        // Guess: a a a a a
        // Marks: PRESENT, ABSENT, ABSENT, ABSENT, ABSENT
        Word guess = new Word("AAAAA");
        Feedback fb = Feedback.of(
            PRESENT, ABSENT, ABSENT, ABSENT, ABSENT
        );

        cs.updatedBy(guess, fb);

        // minCount(a) = 1
        assertEquals(1, cs.minCount.get('A'));

        // maxCount(a) = 1 (mixed marks)
        assertEquals(1, cs.maxCount.get('A'));

        // cannotBe at position 0 (yellow)
        assertTrue(cs.cannotBe[0].contains('A'));
    }

    @Test
    void contradiction_green_then_gray_same_position() {
        ConstraintSet cs = new ConstraintSet();

        Word guess1 = new Word("abcde");
        Feedback fb1 = Feedback.of(
            CORRECT, ABSENT, ABSENT, ABSENT, ABSENT
        );
        cs.updatedBy(guess1, fb1);

        Word guess2 = new Word("abcde");
        Feedback fb2 = Feedback.of(
            ABSENT, ABSENT, ABSENT, ABSENT, ABSENT
        );

        assertThrows(IllegalStateException.class, () -> cs.updatedBy(guess2, fb2));
    }

    @Test
    void contradiction_yellow_then_global_gray() {
        ConstraintSet cs = new ConstraintSet();

        Word guess1 = new Word("abcde");
        Feedback fb1 = Feedback.of(
            PRESENT, ABSENT, ABSENT, ABSENT, ABSENT
        );
        cs.updatedBy(guess1, fb1);

        Word guess2 = new Word("abcde");
        Feedback fb2 = Feedback.of(
            ABSENT, ABSENT, ABSENT, ABSENT, ABSENT
        );

        assertThrows(IllegalStateException.class, () -> cs.updatedBy(guess2, fb2));
    }

    @Test
    void monotonicity_mustBe_cannotBe_mustContain() {
        ConstraintSet cs = new ConstraintSet();

        Word guess1 = new Word("abcde");
        Feedback fb1 = Feedback.of(
            CORRECT, PRESENT, ABSENT, ABSENT, ABSENT
        );
        cs.updatedBy(guess1, fb1);

        // After first update
        assertEquals('A', cs.mustBe[0]);
        assertTrue(cs.mustContain.contains('B'));
        assertTrue(cs.cannotBe[1].contains('B'));

        // Second update tightens constraints
        Word guess2 = new Word("bbcde");
        Feedback fb2 = Feedback.of(
            PRESENT, ABSENT, ABSENT, ABSENT, ABSENT
        );
        cs.updatedBy(guess2, fb2);

        // cannotBe[1] must NOT lose constraints
        assertTrue(cs.cannotBe[1].contains('B')); // still forbidden at pos 1 from earlier yellow
    }
    
    @Test
    void green_conflicts_with_existing_green() {
        ConstraintSet cs = new ConstraintSet();

        cs.updatedBy(new Word("ABCDE"),
                     Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertThrows(IllegalStateException.class, () ->
            cs.updatedBy(new Word("XBCDE"),
                         Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT))
        );
    }

    @Test
    void green_conflicts_with_prior_yellow_forbidding_position() {
        ConstraintSet cs = new ConstraintSet();

        cs.updatedBy(new Word("ABCDE"),
                     Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertThrows(IllegalStateException.class, () ->
            cs.updatedBy(new Word("ABCDE"),
                         Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT))
        );
    }

    @Test
    void yellow_conflicts_with_existing_green_same_position() {
        ConstraintSet cs = new ConstraintSet();

        cs.updatedBy(new Word("ABCDE"),
                     Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertThrows(IllegalStateException.class, () ->
            cs.updatedBy(new Word("ABCDE"),
                         Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT))
        );
    }

    @Test
    void yellow_conflicts_with_global_gray() {
        ConstraintSet cs = new ConstraintSet();

        cs.updatedBy(new Word("ABCDE"),
                     Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertThrows(IllegalStateException.class, () ->
            cs.updatedBy(new Word("ABCDE"),
                         Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT))
        );
    }

    @Test
    void gray_after_yellow_hits_early_return_branch() {
        ConstraintSet cs = new ConstraintSet();

        cs.updatedBy(new Word("ABCDE"),
                     Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT));

        // This should NOT forbid globally — only forbid at this position
        cs.updatedBy(new Word("ABCDE"),
                     Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertTrue(cs.cannotBe[0].contains('A'));
        assertFalse(cs.cannotContain.contains('A'));
    }

    @Test
    void gray_conflicts_with_minCount_from_mixed_marks() {
        ConstraintSet cs = new ConstraintSet();

        cs.updatedBy(new Word("AAAAA"),
                     Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertThrows(IllegalStateException.class, () ->
            cs.updatedBy(new Word("AAAAA"),
                         Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT))
        );
    }

    
    @Test
    void minCount_exceeds_maxCount_triggers_consistency_error() {
        ConstraintSet cs = new ConstraintSet();

        // First: mixed marks so maxCount(a) = 1
        cs.updatedBy(new Word("AAAAA"),
                     Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT));

        // Second: two PRESENT marks so minCount(a) = 2
        assertThrows(IllegalStateException.class, () ->
            cs.updatedBy(new Word("AAAAA"),
                         Feedback.of(PRESENT, PRESENT, ABSENT, ABSENT, ABSENT))
        );
    }

    
    @Test
    void green_then_global_gray_triggers_consistency_error() {
        ConstraintSet cs = new ConstraintSet();

        cs.updatedBy(new Word("ABCDE"),
                     Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertThrows(IllegalStateException.class, () ->
            cs.updatedBy(new Word("ABCDE"),
                         Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT))
        );
    }

}
