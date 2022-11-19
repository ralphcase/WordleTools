import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Position {
	// Constants for the game parameters.
	public static final String LETTERS = "QWERTYUIOPASDFGHJKLZXCVBNM";
	public static final int NUMBERCELLS = 5;
//	private static final String DATAFILE = "5letterwords.txt";
//	private static final String DATAFILE = "5letterwords2.txt";
//	private static final String DATAFILE = "sgb-words.txt";
//	private static final String DATAFILE = "codedict.txt";
	private static final String GOALFILE = "nytGoalList.txt";
	private static final String ALLOWEDFILE = "nytAllowedList.txt";

	
	private String pos;
	private static Random rand = new Random();

	private static List<Position> GOALWORDS = AllPositions(GOALFILE);

	private static List<Position> ALLWORDS = AllPositions(ALLOWEDFILE);
	
	/*
	 * Main constructor
	 */
	public Position(String word) {
		if (word.length() != NUMBERCELLS)
			throw new IllegalArgumentException();
		for (char member : word.toCharArray()) {
			if (LETTERS.indexOf(member) <0)
				throw new IllegalArgumentException();
		}
		pos = word;
	}
	

	/*
	 * Create a random position.
	 */
	public Position() {
		pos = GOALWORDS.get(roll(GOALWORDS.size())).getPos();
	}

	
	/*
	 * Report how well the argument matches this position.
	 */
	public Report guess(Position input) {
		return new Report(this, input);
	}
	
	
	/*
	 * Create a list of all possible positions.
	 * Use words from the given data file.
	 */
	private static List<Position> AllPositions(String datafile) {
		List<Position> result;
		Set<Position> wordlist = new HashSet<Position>();
		String word = "";
		try {
			FileReader reader = new FileReader(datafile);
			int character;
			while ((character = reader.read()) != -1) {
				char letter = Character.toUpperCase((char) character);
				if (LETTERS.indexOf(letter) >= 0)
					word += letter;
				else {
					try {
						wordlist.add(new Position(word));
					} catch (IllegalArgumentException e) {
						// ignore invalid words in the file.
					}
					word = "";
				}
			}
			reader.close();

		} catch (IOException e) {
			System.out.println("word: |" + word + "|");
			e.printStackTrace();
		}
		result = new ArrayList<Position>(wordlist);
		System.out.println(result.size() + " words in dictionary " + datafile);
		return result;
	}

	
	/* 
	 * Remove words that have a letter repeated somewhere in the word.
	 */
	public static List<Position> RemoveDouble(List<Position> input) {
		ArrayList<Position> result = new ArrayList<Position>(input);
		for (Position word : input) {
			Set<Character> letters = new HashSet<Character>();
			for (Character letter: word.toCharArray()) {
				letters.add(letter);
			}
			if (letters.size() < NUMBERCELLS) {
				result.remove(word);
			}
		}
		return result;
	}

	
	/*
	 * Remove words that are an anagram of another word in the list.
	 */
	public static List<Position> RemoveAnagrams(List<Position> input) {
		HashSet<String> unique = new HashSet<String>();
		for (Position word : input) {
			char[] item = word.toCharArray();
			Arrays.sort(item);
			unique.add(new String(item));
		}
		List<Position> result = new ArrayList<Position>();
		for (String word: unique) {
			result.add(getAnagrams(new Position(word)).get(0));
		}
		return result;
	}

	
	/*
	 * Return a list of anagrams of the given word.
	 */
	public static List<Position> getAnagrams(Position input) {
		List<Position> result = new ArrayList<Position>();
		for (Position word: getALLWORDS()) {
			if (input.isAnagram(word))
				result.add(word);
		}
		return result;
	}

	
	private boolean isAnagram(Position target) {
		char[] w1 = this.getPos().toCharArray();
		Arrays.sort(w1);
		char[] w2 = target.getPos().toCharArray();
		Arrays.sort(w2);
		return new String(w1).equals(new String(w2));
	}

	
	private char[] toCharArray() {
		return this.pos.toCharArray();
	}


	public String toString() {
		return getPos();
	}

	
	// Pick a number from 1 to num, inclusive
	private static int roll(int num) {
		return 1 + rand.nextInt(num);
	}

	
	/*
	 * Summarize all letters guessed in all guesses
	 */
	public static Set<Character> allLettersGuessed(List<Guess> allGuesses) {
		Set<Character> found = new HashSet<Character>();
		for (Guess g: allGuesses) {
			for (char letter: g.getPos().getPos().toCharArray())  {
				found.add(letter);
			}
		}
		return found;
	}

	
	public String getPos() {
		return pos;
	}
	
	
	public boolean equals(Position other) {
		return this.toString().equals(other.toString());
	}


	public static List<Position> getGOALWORDS() {
		return GOALWORDS;
	}

	
	public static List<Position> getALLWORDS() {
		// return ALLWORDS.addAll(GOALWORDS);
		List<Position> result = ALLWORDS;
		for (Position word : GOALWORDS) {
			result.add(word);
		}
		return result;
	}

}
