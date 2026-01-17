package position;

import java.util.Arrays;

import report.Report;

public final class Position {

    public static final String LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM";
    public static final int NUMBERCELLS = 5;

    private final char[] pos;

    /**
     * Construct a Position from a 5-letter uppercase word.
     */
    public Position(String word) {
        if (word == null || word.length() != NUMBERCELLS)
            throw new IllegalArgumentException("Word must be exactly 5 letters");

        this.pos = word.toUpperCase().toCharArray();

        for (char c : pos) {
            if (LETTERS.indexOf(c) < 0)
                throw new IllegalArgumentException("Invalid character: " + c);
        }
    }

    /**
     * Produce a Report describing how well the guess matches this position.
     */
    public Report guess(Position input) {
        return new Report(this, input);
    }

    /**
     * Return a defensive copy of the underlying character array.
     */
    public char[] toCharArray() {
        return pos.clone();
    }

    @Override
    public String toString() {
        return new String(pos);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Position))
            return false;
        Position that = (Position) other;
        return Arrays.equals(this.pos, that.pos);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pos);
    }
}