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
		}
	
	static class EntryCompare implements Comparator<Entry<Character, Integer>> {
		@Override
	    public int compare(Entry<Character, Integer> o1, Entry<Character, Integer> o2) {
	        return o2.getValue().compareTo(o1.getValue());
	    }
	}
	
	public static List<Entry<Character, Integer>> letterFrequency(List<Position> words) {
		Map<Character, Integer> frequencyMap = new HashMap<Character, Integer>();
		for (Position word : words) {
			for (Character c : word.toCharArray()) {
				frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
			}
		}
		List<Map.Entry<Character, Integer>> frequencyList = new ArrayList<Map.Entry<Character, Integer>>(frequencyMap.entrySet());
		Collections.sort(frequencyList, new EntryCompare());
		return frequencyList;
	}

}
