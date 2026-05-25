package feedback;

import org.junit.jupiter.api.Test;
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
}
