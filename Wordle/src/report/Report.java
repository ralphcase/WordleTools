package report;

import java.util.Arrays;
import position.Position;

public final class Report {

    public enum Hint {
        ABSENT, PRESENT, CORRECT
    }

    private final Hint[] result;

    /**
     * Construct a Report by comparing a target Position with a guess Position.
     */
    public Report(Position target, Position guess) {
        this.result = computeHints(target, guess);
    }

    /**
     * Internal constructor for tests or solver logic.
     */
    public Report(Hint[] hints) {
        if (hints.length != Position.NUMBERCELLS)
            throw new IllegalArgumentException("Hint array must have length " + Position.NUMBERCELLS);
        this.result = hints.clone();
    }

    private Hint[] computeHints(Position target, Position guess) {
        Hint[] out = new Hint[Position.NUMBERCELLS];
        boolean[] matched = new boolean[Position.NUMBERCELLS];

        char[] t = target.toCharArray();
        char[] g = guess.toCharArray();

        // First pass: exact matches
        for (int i = 0; i < Position.NUMBERCELLS; i++) {
            if (t[i] == g[i]) {
                out[i] = Hint.CORRECT;
                matched[i] = true;
            }
        }

        // Second pass: present letters
        for (int i = 0; i < Position.NUMBERCELLS; i++) {
            if (out[i] == Hint.CORRECT) continue;

            for (int j = 0; j < Position.NUMBERCELLS; j++) {
                if (!matched[j] && t[j] == g[i] && i != j) {
                    out[i] = Hint.PRESENT;
                    matched[j] = true;
                    break;
                }
            }
        }

        // Third pass: absent letters
        for (int i = 0; i < Position.NUMBERCELLS; i++) {
            if (out[i] == null) {
                out[i] = Hint.ABSENT;
            }
        }

        return out;
    }

    public Hint[] getHints() {
        return result.clone();
    }

    public Hint getHint(int index) {
        return result[index];
    }

    public boolean isSolved() {
        for (Hint h : result) {
            if (h != Hint.CORRECT) return false;
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Report)) return false;
        Report that = (Report) other;
        return Arrays.equals(this.result, that.result);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(result);
    }

    @Override
    public String toString() {
        return Arrays.toString(result);
    }
}