package feedback;

import java.util.Arrays;

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
}
