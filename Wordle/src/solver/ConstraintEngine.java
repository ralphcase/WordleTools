package solver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import position.Position;
import report.Report;
import report.Report.Hint;

public final class ConstraintEngine {

    /**
     * Filter the candidate list based on a guess and its feedback report.
     */
    public List<Position> filter(List<Position> candidates, Position guess, Report report) {
        List<Position> result = new ArrayList<>();
        for (Position candidate : candidates) {
            if (matchesConstraints(candidate, guess, report)) {
                result.add(candidate);
            }
        }
        return result;
    }

    /**
     * Check whether a candidate word is consistent with the given guess + report.
     */
    private boolean matchesConstraints(Position candidate, Position guess, Report report) {
        char[] c = candidate.toCharArray();
        char[] g = guess.toCharArray();
        Hint[] hints = report.getHints();

        // First pass: enforce CORRECT positions and count required letters
        Map<Character, Integer> minCounts = new HashMap<>();
        Map<Character, Integer> maxCounts = new HashMap<>();

        for (int i = 0; i < g.length; i++) {
            char letter = g[i];
            Hint hint = hints[i];

            switch (hint) {
                case CORRECT:
                    if (c[i] != letter) {
                        return false;
                    }
                    increment(minCounts, letter);
                    break;

                case PRESENT:
                    if (c[i] == letter) {
                        return false; // must be elsewhere
                    }
                    increment(minCounts, letter);
                    break;

                case ABSENT:
                    // We'll handle ABSENT after we know minCounts
                    break;
            }
        }

        // Count occurrences of guess letters in candidate
        Map<Character, Integer> candidateCounts = new HashMap<>();
        for (char ch : c) {
            increment(candidateCounts, ch);
        }

        // Enforce minimum counts (from CORRECT + PRESENT)
        for (Map.Entry<Character, Integer> e : minCounts.entrySet()) {
            char letter = e.getKey();
            int required = e.getValue();
            int actual = candidateCounts.getOrDefault(letter, 0);
            if (actual < required) {
                return false;
            }
        }

        // Second pass: handle ABSENT letters (no extra occurrences beyond minCounts)
        for (int i = 0; i < g.length; i++) {
            char letter = g[i];
            Hint hint = hints[i];

            if (hint == Hint.ABSENT) {
                int required = minCounts.getOrDefault(letter, 0);
                int actual = candidateCounts.getOrDefault(letter, 0);
                if (actual > required) {
                    return false;
                }
            }
        }

        return true;
    }

    private void increment(Map<Character, Integer> map, char key) {
        map.put(key, map.getOrDefault(key, 0) + 1);
    }
}