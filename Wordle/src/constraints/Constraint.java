package constraints;

import feedback.Feedback;
import feedback.Mark;
import word.Word;

public final class Constraint {

    private final Word guess;
    private final Feedback feedback;

    public Constraint(Word guess, Feedback feedback) {
        if (guess == null) throw new IllegalArgumentException("Guess cannot be null");
        if (feedback == null) throw new IllegalArgumentException("Feedback cannot be null");
        this.guess = guess;
        this.feedback = feedback;
    }

    public boolean allows(Word candidate) {
        char[] guessLetters = guess.letters();
        char[] candidateLetters = candidate.letters();
        Mark[] marks = feedback.marks();

        // ------------------------------------------------------------
        // Precompute guess letter stats (one pass)
        // ------------------------------------------------------------
        int[] guessCount = new int[26];
        int[] positiveCount = new int[26];
        int[] absentCount = new int[26];

        boolean[] seen = new boolean[26];
        char[] distinct = new char[5];
        int distinctCount = 0;

        for (int i = 0; i < Word.LENGTH; i++) {
            char g = guessLetters[i];
            int idx = g - 'A';

            if (!seen[idx]) {
                seen[idx] = true;
                distinct[distinctCount++] = g;
            }

            guessCount[idx]++;
            if (marks[i] == Mark.CORRECT || marks[i] == Mark.PRESENT)
                positiveCount[idx]++;
            else
                absentCount[idx]++;
        }

        // ------------------------------------------------------------
        // Precompute candidate letter counts (cached in Word)
        // ------------------------------------------------------------
        int[] candidateCount = candidate.letterCounts();

        // ------------------------------------------------------------
        // 1. Positional rules (CORRECT, PRESENT, ABSENT)
        // ------------------------------------------------------------
        for (int i = 0; i < Word.LENGTH; i++) {
            char letter = guessLetters[i];
            int idx = letter - 'A';

            switch (marks[i]) {
                case CORRECT -> {
                    if (candidateLetters[i] != letter)
                        return false;
                }
                case PRESENT -> {
                    if (candidateLetters[i] == letter)
                        return false; // cannot be in same position
                    if (candidateCount[idx] == 0)
                        return false;
                }
                case ABSENT -> {
                    if (candidateLetters[i] == letter)
                        return false;
                    if (candidateCount[idx] == 0)
                        break; // candidate avoids letter entirely — OK
                    if (positiveCount[idx] == 0)
                        return false; // pure gray — letter must not appear
                }
            }
        }

        // ------------------------------------------------------------
        // 2. Duplicate-letter rules (only for letters in the guess)
        // ------------------------------------------------------------
        for (int k = 0; k < distinctCount; k++) {
            char letter = distinct[k];
            int idx = letter - 'A';

            int pos = positiveCount[idx];
            int abs = absentCount[idx];
            int cand = candidateCount[idx];

            // Case 1: All marks for this letter are ABSENT
            if (pos == 0) {
                if (cand > 0) return false;
                continue;
            }

            // Case 2: Mixed marks (some PRESENT/CORRECT, some ABSENT)
            if (abs > 0) {
                if (cand != pos) return false;
                continue;
            }

            // Case 3: All marks are PRESENT or CORRECT
            if (cand < pos) return false;
        }

        return true;
    }

    public String toString() {
        return guess + "\t" + feedback;
    }
}
