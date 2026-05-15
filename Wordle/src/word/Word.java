package word;

import java.util.Objects;

public final class Word {

    private final String text;
    public static final int LENGTH = 5;

    public Word(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Word cannot be null or blank");
        }
        this.text = text.trim().toUpperCase();
        if (this.text.length() != LENGTH) {
            throw new IllegalArgumentException("Word must be " + LENGTH + " letters");
        }
    }

    public String text() {
        return text;
    }
    
    public boolean contains(char c) {
    	return text.indexOf(Character.toUpperCase(c)) >= 0;
    }

    @Override
    public String toString() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word other)) return false;
        return text.equals(other.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }

	public char[] letters() {
		char[] result = new char[Word.LENGTH];
		for (int i = 0; i < Word.LENGTH; i++) {
			result[i] = text.charAt(i);
		}
		return result;
	}

	public int count(char letter) {
	    letter = Character.toUpperCase(letter);
        int count = 0;
	    for (int i = 0; i < text.length(); i++) {
	        if (text.charAt(i) == letter) count++;
	    }
	    return count;
	}
}
