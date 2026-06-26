package word;

import java.util.Arrays;

/**
 * A Word in Wordle is upper case and has five letters.
 */
public final class Word {

  public static final int LENGTH = 5;
  private char[] letters;

  private int[] counts; // lazily initialized

  /**
   * Create a word from a string. The string is trimmed and converted to upper case.
   *
   * @param text the string to create the word from
   *             Exceptions are thrown if the string is null, blank, or not of length 5.
   */
  public Word(String text) {
    if (text == null || text.isBlank()) {
      throw new IllegalArgumentException("Word cannot be null or blank");
    }
    text = text.trim().toUpperCase();
    if (text.length() != LENGTH) {
      throw new IllegalArgumentException("Word must be " + LENGTH + " letters");
    }
    this.letters = text.toCharArray();
  }

  /**
   * Does this word contain a given character?
   *
   * @param target the chacter to search for
   * @return true if this word contains the given char.
   */
  public boolean contains(char target) {
    target = Character.toUpperCase(target);
    for (char ch : letters) {
      if (ch == target) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String toString() {
    return new String(this.letters);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) { return true; }
    if (!(obj instanceof Word other)) { return false; }
    return Arrays.equals(this.letters, other.letters);
  }

  @Override
  public int hashCode() {
    return Arrays.hashCode(letters);
  }

  /**
   * Returns the letters of the Word as an array.
   * @return the value of the Word.
   */
  public char[] letters() {
    return letters;
  }

  public char charAt(int index) {
    return letters[index];
  }

  /**
   * Returns a cached array of size 26 containing the count of each letter A–Z.
   * This array is computed once and reused.
   */
  public int[] letterCounts() {
    if (counts == null) {
      int[] alphabet = new int[26];
      for (char ch : letters) {
        alphabet[ch - 'A']++;
      }
      counts = alphabet;
    }
    return counts;
  }
}
