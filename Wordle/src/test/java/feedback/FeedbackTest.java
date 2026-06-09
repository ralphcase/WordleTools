package feedback;

import org.junit.jupiter.api.Test;
import word.Word;

import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    void constructsWithValidMarks() {
        Feedback fb = Feedback.of(
                Mark.CORRECT, Mark.PRESENT, Mark.ABSENT,
                Mark.ABSENT, Mark.CORRECT
        );
        assertEquals(5, fb.marks().length);
    }

    @Test
    void rejectsNullArray() {
        assertThrows(NullPointerException.class,
                () -> Feedback.of((Mark[]) null));
    }

    @Test
    void rejectsWrongLength() {
        assertThrows(IllegalArgumentException.class,
                () -> Feedback.of(Mark.CORRECT)); // only 1 mark
    }

    @Test
    void rejectsNullElements() {
        assertThrows(NullPointerException.class,
                () -> Feedback.of(
                        Mark.CORRECT, null, Mark.ABSENT,
                        Mark.ABSENT, Mark.CORRECT
                ));
    }

    @Test
    void equalityBasedOnMarks() {
        Feedback a = Feedback.of(
                Mark.CORRECT, Mark.PRESENT, Mark.ABSENT,
                Mark.ABSENT, Mark.CORRECT
        );
        Feedback b = Feedback.of(
                Mark.CORRECT, Mark.PRESENT, Mark.ABSENT,
                Mark.ABSENT, Mark.CORRECT
        );
        assertEquals(a, b);
    }


    @Test
    void from_allCorrect() {
        Word goal = new Word("CRANE");
        Word guess = new Word("CRANE");

        Feedback fb = Feedback.from(guess, goal);

        assertArrayEquals(
                new Mark[]{Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT},
                fb.marks()
        );
    }

    @Test
    void from_allAbsent() {
        Word goal = new Word("CRANE");
        Word guess = new Word("TULIP");

        Feedback fb = Feedback.from(guess, goal);

        assertArrayEquals(
                new Mark[]{Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT},
                fb.marks()
        );
    }

    @Test
    void from_presentButWrongPosition() {
        Word goal = new Word("CRANE");
        Word guess = new Word("NACRE"); // all letters present but shuffled

        Feedback fb = Feedback.from(guess, goal);

        assertArrayEquals(
                new Mark[]{
                        Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.PRESENT, Mark.CORRECT
                },
                fb.marks()
        );
    }

    @Test
    void from_mixedMarks() {
        Word goal = new Word("CRANE");
        Word guess = new Word("TRACE");

        Feedback fb = Feedback.from(guess, goal);

        assertArrayEquals(
                new Mark[]{
                        Mark.ABSENT, // T not in goal
                        Mark.CORRECT, // R
                        Mark.CORRECT, // A
                        Mark.PRESENT,  // C
                        Mark.CORRECT  // E
                },
                fb.marks()
        );
    }

    @Test
    void from_handlesRepeatedLettersInGuess() {
        Word goal = new Word("CRANE");
        Word guess = new Word("CARRY"); // two R's, only one in goal

        Feedback fb = Feedback.from(guess, goal);

        assertArrayEquals(
                new Mark[]{
                        Mark.CORRECT, // C
                        Mark.PRESENT, // A
                        Mark.PRESENT, // R
                        Mark.ABSENT,  // second R should be ABSENT
                        Mark.ABSENT   // Y not in goal
                },
                fb.marks()
        );
    }

    @Test
    void from_handlesRepeatedLettersInGoal() {
        Word goal = new Word("SHEEP"); // two E's
        Word guess = new Word("PEELS");

        Feedback fb = Feedback.from(guess, goal);

        assertArrayEquals(
                new Mark[]{
                        Mark.PRESENT, // P is present at end
                        Mark.PRESENT, // E
                        Mark.CORRECT, // E
                        Mark.ABSENT,  // L not in goal
                        Mark.PRESENT  // S present at start
                },
                fb.marks()
        );
    }

    @Test
    public void testEquals() {
        Feedback f = Feedback.of(
                Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT
        );
        Feedback g = Feedback.of(
                Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT
        );
        Feedback h = Feedback.of(
                Mark.ABSENT, Mark.CORRECT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT
        );
        assertEquals(f, f);
        assertEquals(f, g);
        assertNotEquals(f, h);
        assertEquals(f.hashCode(), f.hashCode());
        assertNotEquals(f.hashCode(), h.hashCode());
    }

    @Test
    public void testEqualsWithNonFeedbackObject() {
        Feedback f = Feedback.of(
                Mark.CORRECT, Mark.ABSENT, Mark.PRESENT, Mark.ABSENT, Mark.CORRECT
        );

        assertNotEquals(f, new Object());    // arbitrary object
        assertNotEquals(f,null);            // null check
    }
    @Test
    void isSolvedReturnsTrueWhenAllMarksAreCorrect() {
        Feedback fb = Feedback.of(
                Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT, Mark.CORRECT
        );
        assertTrue(fb.isSolved());
    }

    @Test
    void isSolvedReturnsFalseWhenAnyMarkIsNotCorrect() {
        Feedback fb = Feedback.of(
                Mark.CORRECT, Mark.PRESENT, Mark.CORRECT, Mark.ABSENT, Mark.CORRECT
        );
        assertFalse(fb.isSolved());
    }

    @Test
    void isSolvedReturnsFalseWhenNoMarksAreCorrect() {
        Feedback fb = Feedback.of(
                Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT, Mark.ABSENT
        );
        assertFalse(fb.isSolved());
    }
    @Test
    void toStringIncludesAnsiColoring() {
        Feedback fb = Feedback.of(
                Mark.ABSENT,
                Mark.PRESENT,
                Mark.CORRECT,
                Mark.ABSENT,
                Mark.CORRECT
        );

        String expected =
                "[" +
                        Feedback.COLOR_MAP.get(Mark.ABSENT)   + "ABSENT"  + Feedback.ANSI_RESET + ", " +
                        Feedback.COLOR_MAP.get(Mark.PRESENT)  + "PRESENT" + Feedback.ANSI_RESET + ", " +
                        Feedback.COLOR_MAP.get(Mark.CORRECT)  + "CORRECT" + Feedback.ANSI_RESET + ", " +
                        Feedback.COLOR_MAP.get(Mark.ABSENT)   + "ABSENT"  + Feedback.ANSI_RESET + ", " +
                        Feedback.COLOR_MAP.get(Mark.CORRECT)  + "CORRECT" + Feedback.ANSI_RESET +
                        "]";

        assertEquals(expected, fb.toString());
    }

}
