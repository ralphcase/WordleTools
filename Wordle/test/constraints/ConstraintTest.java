package constraints;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static feedback.Mark.*;


import feedback.Feedback;
import word.Word;

public class ConstraintTest {

    // ------------------------------------------------------------
    // 1. CORRECT tests
    // ------------------------------------------------------------

    @Test
    void correctLetterMustMatchPosition() {
        Constraint c = new Constraint(
            new Word("ABCDE"),
            Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT)
        );

        assertTrue(c.allows(new Word("AXXXX")));
        assertFalse(c.allows(new Word("ZXXXX")));
    }

    @Test
    void correctLetterDoesNotAllowDifferentLetterElsewhere() {
        Constraint c = new Constraint(
            new Word("ABCDE"),
            Feedback.of(ABSENT, CORRECT, ABSENT, ABSENT, ABSENT)
        );

        assertTrue(c.allows(new Word("XBXZZ")));
        assertFalse(c.allows(new Word("XCXZZ"))); // wrong at position 1
    }

    // ------------------------------------------------------------
    // 2. PRESENT tests
    // ------------------------------------------------------------

    @Test
    void presentLetterMustAppearElsewhere() {
        Constraint c = new Constraint(
            new Word("ABCDE"),
            Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT)
        );

        assertTrue(c.allows(new Word("FAAGH"))); // your example
        assertFalse(c.allows(new Word("FXXGH"))); // no A at all
    }

    @Test
    void presentLetterCannotAppearInSamePosition() {
        Constraint c = new Constraint(
            new Word("ABCDE"),
            Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT)
        );

        assertFalse(c.allows(new Word("AXXXX"))); // A in forbidden position
    }

    // ------------------------------------------------------------
    // 3. ABSENT tests (pure gray)
    // ------------------------------------------------------------

    @Test
    void absentLetterMustNotAppearAnywhereIfPureGray() {
        Constraint c = new Constraint(
            new Word("ABCDE"),
            Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT)
        );

        assertFalse(c.allows(new Word("AXXXX"))); // A forbidden
        assertTrue(c.allows(new Word("XXXXX")));  // no forbidden letters
    }

    // ------------------------------------------------------------
    // 4. Mixed ABSENT + PRESENT for same letter
    // ------------------------------------------------------------

    @Test
    void mixedMarksForLetterEnforceExactCount() {
        // Guess: AABCD
        // Feedback: PRESENT, ABSENT, ABSENT, ABSENT, ABSENT
        // -> exactly one A must appear in candidate
        Constraint c = new Constraint(
            new Word("AABCD"),
            Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT)
        );

        assertTrue(c.allows(new Word("XXAXX")));  // exactly one A
        assertFalse(c.allows(new Word("XAXXX")));  // exactly one A where not allowed
        assertFalse(c.allows(new Word("XAAXX"))); // two A's : invalid
        assertFalse(c.allows(new Word("XXAAX"))); // two A's : invalid
        assertFalse(c.allows(new Word("XXXXX"))); // zero A's : invalid
    }

    // ------------------------------------------------------------
    // 5. Duplicate letters, all positive marks
    // ------------------------------------------------------------

    @Test
    void allPositiveMarksRequireAtLeastThatManyOccurrences() {
        // Guess: AABCD
        // Feedback: PRESENT, PRESENT, ABSENT, ABSENT, ABSENT
        // -> at least two A's must appear
        Constraint c = new Constraint(
            new Word("AABCD"),
            Feedback.of(PRESENT, PRESENT, ABSENT, ABSENT, ABSENT)
        );

        assertTrue(c.allows(new Word("XXAAX")));  // valid
        assertTrue(c.allows(new Word("XXAAZ")));  // valid
        assertFalse(c.allows(new Word("XAXXX"))); // only one A -> invalid
        assertFalse(c.allows(new Word("XAAAX"))); // invalid (A at pos1)
        assertFalse(c.allows(new Word("XAAXX"))); // invalid (A at pos1)
        assertFalse(c.allows(new Word("XXAXX"))); // only one A

    }

    // ------------------------------------------------------------
    // 6. Pure gray for duplicate letters
    // ------------------------------------------------------------

    @Test
    void pureGrayDuplicateLetterMustNotAppear() {
        // Guess: AABCD
        // Feedback: ABSENT, ABSENT, ABSENT, ABSENT, ABSENT
        // -> zero A's allowed
        Constraint c = new Constraint(
            new Word("AABCD"),
            Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT)
        );

        assertFalse(c.allows(new Word("XAAXX"))); // contains A
        assertTrue(c.allows(new Word("XXXXX")));  // no A
    }

    // ------------------------------------------------------------
    // 7. Mixed marks for other letters
    // ------------------------------------------------------------

    @Test
    void mixedMarksForNonDuplicateLettersBehaveCorrectly() {
        // Guess: ABCDE
        // Feedback: ABSENT, PRESENT, ABSENT, ABSENT, ABSENT
        // -> B must appear somewhere except position 1
        Constraint c = new Constraint(
            new Word("ABCDE"),
            Feedback.of(ABSENT, PRESENT, ABSENT, ABSENT, ABSENT)
        );

        assertFalse(c.allows(new Word("XBXXX")));  // B at pos 2        
        assertTrue(c.allows(new Word("XXXXB")));  // B at pos 4        
        assertTrue(c.allows(new Word("XXBBX"))); // B not at pos 
        assertFalse(c.allows(new Word("XBXBX"))); // B at pos 1 -> invalid
        assertFalse(c.allows(new Word("XXXXX"))); // no B -> invalid
    }
}
