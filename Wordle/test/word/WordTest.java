package word;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    @Test
    void constructsWithValidText() {
        Word w = new Word("apple");
        assertEquals("APPLE", w.text());
    }

    @Test
    void trimsAndLowercases() {
        Word w = new Word("  Apple  ");
        assertEquals("APPLE", w.text());
    }

    @Test
    void rejectsNullOrBlank() {
        assertThrows(IllegalArgumentException.class, () -> new Word(null));
        assertThrows(IllegalArgumentException.class, () -> new Word(""));
        assertThrows(IllegalArgumentException.class, () -> new Word("   "));
    }

    @Test
    void equalityBasedOnText() {
        assertEquals(new Word("apple"), new Word("APPLE"));
        assertNotEquals(new Word("apple"), new Word("apply"));
    }
}
