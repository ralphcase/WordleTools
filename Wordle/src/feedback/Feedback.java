package feedback;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import word.Word;

public final class Feedback {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_GRAY = "\u001B[90m";
    private static final Map<Mark, String> COLOR_MAP = Map.of(
            Mark.ABSENT, ANSI_GRAY,
            Mark.PRESENT, ANSI_YELLOW,
            Mark.CORRECT, ANSI_GREEN
    );

    public static final int LENGTH = 5;

    private final Mark[] marks;

    private Feedback(Mark[] marks) {
        this.marks = marks;
    }

    public static Feedback of(Mark... marks) {
        Objects.requireNonNull(marks);
        if (marks.length != Word.LENGTH) throw new IllegalArgumentException();
        for (Mark m : marks) Objects.requireNonNull(m);
        return new Feedback(marks.clone());
    }
    
    public static Feedback from(Word guess, Word target) {
        Mark[] marks = new Mark[Word.LENGTH];

        // First pass: CORRECT marks
        boolean[] used = new boolean[Word.LENGTH];
        for (int i = 0; i < Word.LENGTH; i++) {
            if (guess.text().charAt(i) == target.text().charAt(i)) {
                marks[i] = Mark.CORRECT;
                used[i] = true;
            }
        }

        // Second pass: PRESENT or ABSENT
        for (int i = 0; i < Word.LENGTH; i++) {
            if (marks[i] == Mark.CORRECT) continue;

            char g = guess.text().charAt(i);
            boolean found = false;

            for (int j = 0; j < Word.LENGTH; j++) {
                if (!used[j] && target.text().charAt(j) == g) {
                    found = true;
                    used[j] = true;
                    break;
                }
            }

            marks[i] = found ? Mark.PRESENT : Mark.ABSENT;
        }

        return Feedback.of(marks);
    }

    public Mark[] marks() {
        return Arrays.copyOf(marks, LENGTH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Feedback other)) {
            return false;
        }
        return Arrays.equals(this.marks, other.marks);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(marks);
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append('[');
        for (int i = 0; i < marks.length; i++) {
            Mark mark = marks[i];
            out.append(COLOR_MAP.get(mark));
            out.append(mark);
            out.append(ANSI_RESET);
            if (i < marks.length - 1) {
                out.append(", ");
            }
        }
        out.append(']');
        return out.toString();
    }

    public boolean hasAnyPresentOrCorrect(Word guess, char letter) {
        for (int i = 0; i < marks.length; i++) {
            if (guess.letters()[i] == letter &&
                (marks[i] == Mark.PRESENT || marks[i] == Mark.CORRECT)) {
                return true;
            }
        }
        return false;
    }
}
