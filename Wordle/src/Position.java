import java.io.FileReader;
import java.io.FileWriter;
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
//	private static final String GOALFILE = "nytGoalList.txt";
//	private static final String ALLOWEDFILE = "nytAllowedList.txt";
//	private static final String GOALFILE = "nytDictionary.txt";
//	private static final String ALLOWEDFILE = "nytDictionary.txt";
	private static final String GOALFILE = "possibleGoals.txt";
	private static final String ALLOWEDFILE = "nonGoals.txt";

	private static final String WORDLEBOT = "wordlebot.txt";

	
	private char[] pos;
	private static Random rand = new Random();

	private static List<Position> GOALWORDS = AllPositions(GOALFILE);

	private static List<Position> ALLWORDS = AllPositions(ALLOWEDFILE);
	
	/*
	 * Main constructor
	 */
	public Position(String word) {
		if (word.length() != NUMBERCELLS)
			throw new IllegalArgumentException();
		pos = word.toCharArray();
		for (char member : pos) {
			if (LETTERS.indexOf(member) < 0)
				throw new IllegalArgumentException();
		}
	}
	
	/*
	 * Create a random position.
	 */
	public Position() {
		pos = GOALWORDS.get(roll(GOALWORDS.size())).toCharArray();
	}

	
	/*
	 * Report how well the argument matches this position.
	 */
	public Report guess(Position input) {
		return new Report(this, input);
	}
	
	private static void writeWords(String wordlist, String filename) {
		try (FileWriter writer = new FileWriter(filename)) {
			writer.write(wordlist);
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	
	public char[] toCharArray() {
		return this.pos;
	}


	public String toString() {
		return new String(pos);
	}

	
	// Pick a number from 1 to num, inclusive
	private static int roll(int num) {
		return 1 + rand.nextInt(num);
	}

	
	public boolean equals(Position other) {
		return Arrays.equals(this.pos, other.pos);
	}


	public static List<Position> getGOALWORDS() {
		return GOALWORDS;
	}

	
	public static List<Position> getALLWORDS() {
		Set<Position> result = new HashSet<Position>(ALLWORDS);
		for (Position word : GOALWORDS) {
			result.add(word);
		}
		return new ArrayList<Position>(result);
	}
	
	
	/*
	 * Methods below here are not part of the function. They are tools created to help analyze word lists. 
	 */

	
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
		char[] w1 = this.toCharArray();
		Arrays.sort(w1);
		char[] w2 = target.toCharArray();
		Arrays.sort(w2);
		return Arrays.equals(w1, w2);
	}

	
	public String whichList() {
		if (Position.indexOf(ALLWORDS, this) >= 0) return "ALLWORDS";
		if (Position.indexOf(GOALWORDS, this) >= 0) return "GOALWORDS";
		return "none";
	}
	
	public static void wordlists() {
		Set<String> allWords = new HashSet<String>(toStrings(AllPositions(ALLOWEDFILE)));
//		Set<String> nytAllowedList = new HashSet<String>(toStrings(AllPositions("nytAllowedList.txt")));
//		Set<String> nytGoalList = new HashSet<String>(toStrings(AllPositions("nytGoalList.txt")));
		Set<String> possibleGoals = new HashSet<String>(toStrings(AllPositions(GOALFILE)));
		Set<String> wordlebot = new HashSet<String>(toStrings(AllPositions(WORDLEBOT)));
		
		System.out.println("possible goals: " + possibleGoals.size());
		possibleGoals.addAll(wordlebot);
		System.out.println("possible goals with new wordlebot: " + possibleGoals.size());
		writeWords(formatList(possibleGoals), GOALFILE);
		
		System.out.println("all words: "+allWords.size());
		allWords.removeAll(possibleGoals);
		System.out.println("updated words: " + allWords.size());
		writeWords(formatList(allWords), ALLOWEDFILE);
		
		
		
//		Set<String> oldDictionary = new HashSet<String>();
//		oldDictionary.addAll(nytAllowedList);
//		oldDictionary.addAll(nytGoalList);
//		System.out.println("oldDictionary: "+oldDictionary.size());
//		System.out.println("nytDictionary: "+nytDictionary.size());
		
//		nytDictionary.removeAll(oldDictionary);
//		System.out.println("in new dictionary but not in old: " + nytDictionary.size());
//		oldDictionary.removeAll(nytDictionary);
//		System.out.println("in old dictionary but not in new: " + oldDictionary.size());

		
		
//		nytGoalList.addAll(possibleGoals);
//		System.out.println("new goals: "+nytGoalList.size());
//		System.out.println(goals);
		
//		nytDictionary.addAll(nytAllowedList);
//		nytDictionary.removeAll(nytGoalList);
//		System.out.println("allowed words: "+nytDictionary.size());
		
//		nytDictionary.removeAll(nytAllowedList);
		
//		System.out.println("in dictionary but not in new allowed: " + nytDictionary.size());
//		System.out.println(nytDictionary);
		
		
	}
	
	private static String formatList(Set<String> input) {
		StringBuilder output = new StringBuilder();
		int lineLength = 22;
		int i = 0;
		for (String word : input) {
			i++;
			output.append(word);
			output.append(' ');
			if (i % lineLength == 0) {
				output.append("\n");
			}
		}
		return output.toString();
	}

	private static List<String> toStrings(List<Position> input) {
		ArrayList<String> result = new ArrayList<String>();
		for (Position word : input) 
			result.add(word.toString());
		return result;
	}
	

	public static void splitAllGoalWords() {
		Position target = new Position("ZYMIC");
		int pivot = Position.indexOf(ALLWORDS, target);
		System.out.println(pivot);
		
		GOALWORDS = ALLWORDS.subList(pivot+1, ALLWORDS.size());
		ALLWORDS = ALLWORDS.subList(0, pivot);
		System.out.println("ALLWORDS has "+ALLWORDS.size());
		System.out.println("GOALWORDS has "+GOALWORDS.size());
	}

	/*
	 * Summarize all letters guessed in all guesses
	 */
	public static Set<Character> allLettersGuessed(List<Guess> allGuesses) {
		Set<Character> found = new HashSet<Character>();
		for (Guess g: allGuesses) {
			for (char letter: g.getPos().toCharArray())  {
				found.add(letter);
			}
		}
		return found;
	}
	
	private static int indexOf(List<Position> dict, Position target) {
		int pivot = -1;
		for (int i = 0; i < dict.size(); i++) {
			if (dict.get(i).equals(target)) {
				pivot = i;
				break;
			}
		}
		return pivot;
	}

}
