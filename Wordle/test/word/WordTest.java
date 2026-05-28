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
    void trimsAndUppercases() {
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
    void lettersReturnsNewArrayEachTime() {
        Word w = new Word("APPLE");
        char[] letters1 = w.letters();
        char[] letters2 = w.letters();
        assertNotSame(letters1, letters2); // defensive copy
        assertArrayEquals(letters1, letters2); // but content is same
    }

    @Test
    void lettersMutationDoesNotAffectWord() {
        Word w = new Word("APPLE");
        char[] letters = w.letters();
        letters[0] = 'Z';

        assertEquals('A', w.letters()[0]); // word unchanged
    }

    // ============================================================
    // NEW TESTS: contains() method
    // ============================================================

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

    // ============================================================
    // NEW TESTS: count() method
    // ============================================================

    @Test
    void countReturnsSingleOccurrence() {
        Word w = new Word("CRANE");
        assertEquals(1, w.count('C'));
        assertEquals(1, w.count('R'));
        assertEquals(1, w.count('A'));
        assertEquals(1, w.count('N'));
        assertEquals(1, w.count('E'));
    }

    @Test
    void countReturnsDuplicateOccurrences() {
        Word w = new Word("APPLE");
        assertEquals(1, w.count('A'));
        assertEquals(2, w.count('P'));
        assertEquals(1, w.count('L'));
        assertEquals(1, w.count('E'));
    }

    @Test
    void countReturnsZeroForLetterNotInWord() {
        Word w = new Word("CRANE");
        assertEquals(0, w.count('Z'));
        assertEquals(0, w.count('X'));
    }

    @Test
    void countReturnsManyOccurrences() {
        Word w = new Word("GEESE");
        assertEquals(3, w.count('E'));
        assertEquals(1, w.count('G'));
        assertEquals(0, w.count('Z'));
    }

    @Test
    void countIsCaseInsensitive() {
        Word w = new Word("apple");
        assertEquals(2, w.count('p'));
        assertEquals(2, w.count('P'));
    }

    @Test
    void countHandlesAllLettersInWord() {
        Word w = new Word("ABBEY");
        assertEquals(1, w.count('A'));
        assertEquals(2, w.count('B'));
        assertEquals(1, w.count('E'));
        assertEquals(1, w.count('Y'));
        assertEquals(0, w.count('Z'));
    }

    // ============================================================
    // NEW TESTS: Immutability and defensive copying
    // ============================================================

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

}