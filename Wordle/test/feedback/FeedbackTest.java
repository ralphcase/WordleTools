package feedback;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FeedbackTest {

    @Test
    void constructsWithValidMarks() {
        Feedback fb = new Feedback(new Mark[] {
                Mark.CORRECT, Mark.PRESENT, Mark.ABSENT,
                Mark.ABSENT, Mark.CORRECT
        });
        assertEquals(5, fb.marks().length);
    }

    @Test
    void rejectsNullArray() {
        assertThrows(IllegalArgumentException.class, () -> new Feedback(null));
    }

    @Test
    void rejectsWrongLength() {
        assertThrows(IllegalArgumentException.class,
                () -> new Feedback(new Mark[] { Mark.CORRECT }));
    }

    @Test
    void equalityBasedOnMarks() {
        Feedback a = new Feedback(new Mark[] {
                Mark.CORRECT, Mark.PRESENT, Mark.ABSENT,
                Mark.ABSENT, Mark.CORRECT
        });
        Feedback b = new Feedback(new Mark[] {
                Mark.CORRECT, Mark.PRESENT, Mark.ABSENT,
                Mark.ABSENT, Mark.CORRECT
        });
        assertEquals(a, b);
    }
}
