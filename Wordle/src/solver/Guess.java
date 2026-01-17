package solver;

import position.Position;
import report.Report;

public final class Guess {

    private final Position guess;
    private final Report report;

    public Guess(Position guess, Report report) {
        if (guess == null || report == null) {
            throw new IllegalArgumentException("Guess and report must not be null");
        }
        this.guess = guess;
        this.report = report;
    }

    public Position getPosition() {
        return guess;
    }

    public Report getReport() {
        return report;
    }

    @Override
    public String toString() {
        return guess.toString() + " -> " + report.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Guess)) return false;
        Guess that = (Guess) other;
        return guess.equals(that.guess) && report.equals(that.report);
    }

    @Override
    public int hashCode() {
        return 31 * guess.hashCode() + report.hashCode();
    }
}