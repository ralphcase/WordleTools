package word;

import java.util.Objects;

public final class Word {

    private final String text;
    public static final int LENGTH = 5;

    public Word(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Word cannot be null or blank");
        }
        if (text.length() != LENGTH) {
            throw new IllegalArgumentException("Word must be " + LENGTH + " letters");
        }
        this.text = text.trim().toUpperCase();
    }

    public String text() {
        return text;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;
        Word other = (Word) o;
        return text.equals(other.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}
