package feedback;

import java.util.Arrays;

import word.Word;

public final class Feedback {

    public static final int LENGTH = 5;

    private final Mark[] marks;

    public Feedback(Mark[] marks) {
        if (marks == null) {
            throw new IllegalArgumentException("Marks cannot be null");
        }
        if (marks.length != LENGTH) {
            throw new IllegalArgumentException("Feedback must have length " + LENGTH);
        }
        // Defensive copy to preserve immutability
        this.marks = Arrays.copyOf(marks, LENGTH);
    }
    
    public static Feedback of(Mark... marks) {
        if (marks == null) {
            throw new IllegalArgumentException("Marks array cannot be null");
        }
        if (marks.length != Word.LENGTH) {
            throw new IllegalArgumentException(
                "Feedback must have exactly " + Word.LENGTH + " marks"
            );
        }

        // Defensive copy to preserve immutability
        Mark[] copy = new Mark[Word.LENGTH];
        for (int i = 0; i < Word.LENGTH; i++) {
            if (marks[i] == null) {
                throw new IllegalArgumentException("Mark at index " + i + " is null");
            }
            copy[i] = marks[i];
        }

        return new Feedback(copy);
    }


    public Mark[] marks() {
        return Arrays.copyOf(marks, LENGTH);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feedback)) return false;
        Feedback other = (Feedback) o;
        return Arrays.equals(this.marks, other.marks);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(marks);
    }

    @Override
    public String toString() {
        return Arrays.toString(marks);
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
