package word;

import java.util.Arrays;

public final class Word {

    public static final int LENGTH = 5;
    private final char[] letters;

    private int[] counts; // lazily initialized

    public Word(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Word cannot be null or blank");
        }
        text = text.trim().toUpperCase();
        if (text.length() != LENGTH) {
            throw new IllegalArgumentException("Word must be " + LENGTH + " letters");
        }
        this.letters = text.toCharArray();
    }

    public boolean contains(char c) {
        c = Character.toUpperCase(c);
        for (char x : letters) {
            if (x == c) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return new String(this.letters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word other)) return false;
        return Arrays.equals(this.letters, other.letters);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(letters);
    }

    public char[] letters() {
        return letters;
    }

    public char charAt(int index) {
        return letters[index];
    }

    /**
     * Returns a cached array of size 26 containing the count of each letter A–Z.
     * This array is computed once and reused.
     */
    public int[] letterCounts() {
        if (counts == null) {
            int[] c = new int[26];
            for (char ch : letters) {
                c[ch - 'A']++;
            }
            counts = c;
        }
        return counts;
    }

}
