package word;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WordTest {

    @Test
    void constructsWithValidText() {
        Word w = new Word("apple");
        assertEquals("APPLE", w.toString());
    }

    @Test
    void trimsAndUppercases() {
        Word w = new Word("  Apple  ");
        assertEquals("APPLE", w.toString());
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

    // ============================================================
    // NEW TESTS: letters() method
    // ============================================================

    @Test
    void lettersReturnsCharArray() {
        Word w = new Word("APPLE");
        char[] letters = w.letters();
        assertEquals(5, letters.length);
        assertEquals('A', letters[0]);
        assertEquals('P', letters[1]);
        assertEquals('P', letters[2]);
        assertEquals('L', letters[3]);
        assertEquals('E', letters[4]);
    }


    @Test
    void containsDetectsLetterInWord() {
        Word w = new Word("APPLE");
        assertTrue(w.contains('A'));
        assertTrue(w.contains('P'));
        assertTrue(w.contains('L'));
        assertTrue(w.contains('E'));
    }

    @Test
    void containsReturnsFalseForLetterNotInWord() {
        Word w = new Word("APPLE");
        assertFalse(w.contains('Z'));
        assertFalse(w.contains('B'));
        assertFalse(w.contains('X'));
    }

    @Test
    void containsIsCaseInsensitive() {
        Word w = new Word("apple");
        assertTrue(w.contains('a'));
        assertTrue(w.contains('A'));
    }

    @Test
    void containsHandlesDuplicates() {
        Word w = new Word("ABBEY");
        assertTrue(w.contains('A'));
        assertTrue(w.contains('B'));
        assertTrue(w.contains('E'));
    }

    @Test
    void wordIsImmutable() {
        Word w1 = new Word("CRANE");
        Word w2 = new Word("CRANE");
        assertEquals(w1, w2);
        // No way to mutate w1 through public API
    }

    @Test
    public void testEqualsWithNonWordObject() {
        Word w = new Word("SLATE");
        assertNotEquals(w, "SLATE");     // different type
        assertNotEquals(w, new Object()); // arbitrary object
        assertNotEquals(w, null);         // null check
    }

    @Test
    void letterCountsSimpleWord() {
        Word w = new Word("APPLE");
        int[] counts = w.letterCounts();

        assertEquals(1, counts[0]);
        assertEquals(2, counts['P' - 'A']);
        assertEquals(1, counts['L' - 'A']);
        assertEquals(1, counts['E' - 'A']);

        // all others zero
        for (int i = 0; i < 26; i++) {
            if ("APLE".indexOf('A' + i) < 0) {
                assertEquals(0, counts[i]);
            }
        }
    }

    @Test
    void letterCountsHandlesDuplicates() {
        Word w = new Word("BANAN");
        int[] counts = w.letterCounts();

        assertEquals(2, counts[0]);
        assertEquals(2, counts['N' - 'A']);
        assertEquals(1, counts['B' - 'A']);
    }

    @Test
    void letterCountsIsCached() {
        Word w = new Word("APPLE");
        int[] c1 = w.letterCounts();
        int[] c2 = w.letterCounts();

        assertSame(c1, c2);
    }

    @Test
    void letterCountsIsCaseInsensitive() {
        Word w = new Word("Apple");
        int[] counts = w.letterCounts();

        assertEquals(1, counts[0]);
        assertEquals(2, counts['P' - 'A']);
        assertEquals(1, counts['L' - 'A']);
        assertEquals(1, counts['E' - 'A']);
    }

}