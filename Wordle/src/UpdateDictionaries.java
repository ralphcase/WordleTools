import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class UpdateDictionaries {

	public static void main(String[] args) {
//		debug();
		Position.wordlists();
//		System.out.println(letterFrequency(Position.getGOALWORDS()));
	}

	private static void debug() {
			Position.wordlists();
	//		List<Guess> guesses = new ArrayList<Guess>();
	//		Position.splitAllGoalWords();
	//		
	//		Position testWord = new Position("ECARD");
	//		System.out.println(testWord + " is in " + testWord.whichList());
	//		testWord = new Position("AREDD");
	//		System.out.println(testWord + " is in " + testWord.whichList());
	//		testWord = new Position("BREAD");
	//		System.out.println(testWord + " is in " + testWord.whichList());
	//		testWord = new Position("AREAD");
	//		System.out.println(testWord + " is in " + testWord.whichList());
	//		testWord = new Position("OREAD");
	//		System.out.println(testWord + " is in " + testWord.whichList());
	//
	//		guesses.add(new Guess(new Position("RAISE"), new Report(new ArrayList<String>(List.of("gray", "yellow", "gray", "gray", "gray")))));
	//		guesses.add(new Guess(new Position("CLOAM"), new Report(new ArrayList<String>(List.of("gray", "gray", "yellow", "yellow", "gray")))));
	//		guesses.add(new Guess(new Position("TONGA"), new Report(new ArrayList<String>(List.of("gray", "yellow", "yellow", "yellow", "yellow")))));
	//		
	//		List<Position> words = new ArrayList<Position>();
	//		words.add(new Position("AGONY"));
			
	//		removeImpossible(words, guesses);
			
		}
	
	static class EntryCompare implements Comparator<Entry<Character, Integer>> {
	    public int compare(Entry<Character, Integer> o1, Entry<Character, Integer> o2) {
	        return o2.getValue().compareTo(o1.getValue());
	    }
	}
	
	public static List<Entry<Character, Integer>> letterFrequency(List<Position> words) {
		Map<Character, Integer> result = new HashMap<Character, Integer>();
		for (Position word : words) {
			for (Character c : word.toCharArray()) {
				int num = result.getOrDefault(c, 0);
				result.put(c, num + 1);
			}
		}
		List<Map.Entry<Character, Integer>> list = new ArrayList<Map.Entry<Character, Integer>>(result.entrySet());
		Collections.sort(list, new EntryCompare());
		return list;
	}

}
