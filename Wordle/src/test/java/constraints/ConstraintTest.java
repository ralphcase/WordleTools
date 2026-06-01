package constraints;

import feedback.Feedback;
import org.junit.jupiter.api.Test;
import word.Word;

import static feedback.Mark.*;
import static org.junit.jupiter.api.Assertions.*;

public class ConstraintTest {

    @Test
    void constructorRejectsNullGuess() {
        assertThrows(IllegalArgumentException.class,
                () -> new Constraint(null, Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT)));
    }

    @Test
    void constructorRejectsNullFeedback() {
        assertThrows(IllegalArgumentException.class, () -> new Constraint(new Word("ABCDE"), null));
    }

    @Test
    void allowsRejectsNullCandidate() {
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertThrows(NullPointerException.class, () -> c.allows(null));
    }

    // ------------------------------------------------------------
    // 1. CORRECT tests
    // ------------------------------------------------------------

    @Test
    void correctLetterMustMatchPosition() {
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertTrue(c.allows(new Word("AXXXX")));
        assertFalse(c.allows(new Word("ZXXXX")));
    }

    @Test
    void correctLetterDoesNotAllowDifferentLetterElsewhere() {
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(ABSENT, CORRECT, ABSENT, ABSENT, ABSENT));

        assertTrue(c.allows(new Word("XBXZZ")));
        assertFalse(c.allows(new Word("XCXZZ"))); // wrong at position 1
    }

    // ------------------------------------------------------------
    // 2. PRESENT tests
    // ------------------------------------------------------------

    @Test
    void presentLetterMustAppearElsewhere() {
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertTrue(c.allows(new Word("FAAGH"))); // your example
        assertFalse(c.allows(new Word("FXXGH"))); // no A at all
    }

    @Test
    void presentLetterCannotAppearInSamePosition() {
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertFalse(c.allows(new Word("AXXXX"))); // A in forbidden position
    }

    // ------------------------------------------------------------
    // 3. ABSENT tests (pure gray)
    // ------------------------------------------------------------

    @Test
    void absentLetterMustNotAppearAnywhereIfPureGray() {
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertFalse(c.allows(new Word("AXXXX"))); // A forbidden
        assertTrue(c.allows(new Word("XXXXX"))); // no forbidden letters
        assertFalse(c.allows(new Word("XXAXX"))); // A forbidden
    }

    // ------------------------------------------------------------
    // 4. Mixed ABSENT + PRESENT for same letter
    // ------------------------------------------------------------

    @Test
    void mixedMarksForLetterEnforceExactCount() {
        // Guess: AABCD
        // Feedback: PRESENT, ABSENT, ABSENT, ABSENT, ABSENT
        // -> exactly one A must appear in candidate
        Constraint c = new Constraint(new Word("AABCD"), Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertTrue(c.allows(new Word("XXAXX"))); // exactly one A
        assertFalse(c.allows(new Word("XAXXX"))); // exactly one A where not allowed
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
        Constraint c = new Constraint(new Word("AABCD"), Feedback.of(PRESENT, PRESENT, ABSENT, ABSENT, ABSENT));

        assertTrue(c.allows(new Word("XXAAX"))); // valid
        assertTrue(c.allows(new Word("XXAAZ"))); // valid
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
        Constraint c = new Constraint(new Word("AABCD"), Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertFalse(c.allows(new Word("XAAXX"))); // contains A
        assertTrue(c.allows(new Word("XXXXX"))); // no A
    }

    // ------------------------------------------------------------
    // 7. Mixed marks for other letters
    // ------------------------------------------------------------

    @Test
    void mixedMarksForNonDuplicateLettersBehaveCorrectly() {
        // Guess: ABCDE
        // Feedback: ABSENT, PRESENT, ABSENT, ABSENT, ABSENT
        // -> B must appear somewhere except position 1
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(ABSENT, PRESENT, ABSENT, ABSENT, ABSENT));

        assertFalse(c.allows(new Word("XBXXX"))); // B at pos 2
        assertTrue(c.allows(new Word("XXXXB"))); // B at pos 4
        assertTrue(c.allows(new Word("XXBBX"))); // B not at pos
        assertFalse(c.allows(new Word("XBXBX"))); // B at pos 1 -> invalid
        assertFalse(c.allows(new Word("XXXXX"))); // no B -> invalid
    }

    @Test
    void candidateWithTooFewOccurrencesIsRejected() {
        Constraint c = new Constraint(new Word("AABXX"), Feedback.of(PRESENT, PRESENT, ABSENT, ABSENT, ABSENT));

        // Needs at least two A's, but only has one
        assertFalse(c.allows(new Word("XXAXX")));
    }

    @Test
    void candidateWithTooManyOccurrencesIsRejected() {
        Constraint c = new Constraint(new Word("ABXXX"), Feedback.of(PRESENT, ABSENT, ABSENT, ABSENT, ABSENT));

        // B is PRESENT -> must appear exactly once
        assertFalse(c.allows(new Word("XBBXX"))); // too many B's
    }

    @Test
    void greenMarkRequiresLetterAtExactPosition() {
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(CORRECT, ABSENT, ABSENT, ABSENT, ABSENT));

        assertFalse(c.allows(new Word("XBCDE"))); // A missing at pos0
        assertFalse(c.allows(new Word("ABCDE"))); // We know BCDE are not present
        assertTrue(c.allows(new Word("AXXXX")));  // A at pos0, no B/C/D/E
    }

    @Test
    void presentMarkForbidsLetterAtMarkedPosition() {
        Constraint c = new Constraint(new Word("ABCDE"), Feedback.of(ABSENT, PRESENT, ABSENT, ABSENT, ABSENT));

        assertFalse(c.allows(new Word("XBXXX"))); // B at pos1 - forbidden
        assertTrue(c.allows(new Word("XXXBX"))); // B at pos3 - allowed
    }

    @Test
    void mixedGreenAndPresentForSameLetterBehavesCorrectly() {
        Constraint c = new Constraint(
                new Word("AAXXX"),
                Feedback.of(CORRECT, PRESENT, ABSENT, ABSENT, ABSENT)
        );

        assertFalse(c.allows(new Word("AAXXX"))); // A at pos1 - forbidden
        assertTrue(c.allows(new Word("AYAYY")));  // A at pos0 and pos2 - valid
    }

    @Test
    void multiplePresentMarksForSameLetterRejectsForbiddenExtraOccurrence() {
        Constraint c = new Constraint(
                new Word("AABXX"),
                Feedback.of(PRESENT, PRESENT, ABSENT, ABSENT, ABSENT)
        );

        // A at pos1 - forbidden even though count is OK
        assertFalse(c.allows(new Word("XAAXX")));
    }

    @Test
    void candidateMayContainUnmentionedLettersUnlessForbidden() {
        Constraint c = new Constraint(
                new Word("ABCDE"),
                Feedback.of(ABSENT, ABSENT, ABSENT, ABSENT, ABSENT)
        );

        assertTrue(c.allows(new Word("XXXXX"))); // allowed
        assertFalse(c.allows(new Word("AXXXX"))); // A forbidden
    }

    @Test
    void allGreenRequiresExactMatch() {
        Constraint c = new Constraint(
                new Word("ABCDE"),
                Feedback.of(CORRECT, CORRECT, CORRECT, CORRECT, CORRECT)
        );

        assertTrue(c.allows(new Word("ABCDE")));
        assertFalse(c.allows(new Word("ABCDF")));
    }

}
