package constraints;

import feedback.Feedback;
import word.Word;

public final class ConstraintSet {

    public ConstraintSet() {
        // empty initial constraints
    }

    public boolean allows(Object candidate) {
        // placeholder implementation
        return true;
    }

    public ConstraintSet updatedBy(Word guess, Feedback fb) {
        if (guess.text().length() != Feedback.LENGTH) {
            throw new IllegalArgumentException("Guess and feedback must have the same length");
        }

        // behavior to be added in future commits
        return this;
    }
}
