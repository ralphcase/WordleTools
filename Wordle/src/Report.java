import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.HashMap;


public class Report {

	public enum Hint {
		ABSENT, PRESENT, CORRECT
	}
	static final String ANSI_RESET = "\u001B[0m";
	static final String ANSI_GREEN = "\u001B[32m";
	static final String ANSI_YELLOW = "\u001B[33m";
	static final String ANSI_BLACK = "\u001B[30m";
	static final String ANSI_GRAY = "\u001B[90m";
	static final Map<Hint, String> COLORMAP = Map.of(
			Hint.ABSENT, ANSI_GRAY,
			Hint.PRESENT, ANSI_YELLOW,
			Hint.CORRECT, ANSI_GREEN
			);

	private Hint[] result = new Hint[Position.NUMBERCELLS];

	public Report(Position target, Position guess) {
		boolean[] matched = new boolean[Position.NUMBERCELLS];
		
		// Check for exact matches.
		for (int i = 0; i < Position.NUMBERCELLS; i++)
			if (target.toCharArray()[i] == guess.toCharArray()[i]) {
				result[i] = Hint.CORRECT;
				matched[i] = true;
			}
		
		// Find present letters that aren't exact.
		for (int i = 0; i < Position.NUMBERCELLS; i++)
			// loop through letters in the guess.
			for (int j = 0; j < Position.NUMBERCELLS; j++)
				// loop through unmatched letters in the target.
				if (!matched[j] 
						&& result[i] != Hint.CORRECT
						&& i != j 
						&& target.toCharArray()[j] == guess.toCharArray()[i]) {
					result[i] = Hint.PRESENT;
					matched[j] = true;
					break;
				}
		
		// Anything else must be absent.
		for (int i = 0; i < Position.NUMBERCELLS; i++) {
			if (result[i] == null)
				result[i] = Hint.ABSENT;
		}
	}

	public Report(String[] input) {
		fillResult(input);
	}

	public Report(List<String> input) {
		String[] data = new String[Position.NUMBERCELLS];
		fillResult(input.toArray(data));
	}

	private void fillResult(String[] input) {
		for (int i = 0; i < Position.NUMBERCELLS; i++) {
			if (input[i].equalsIgnoreCase("gray") || input[i].equalsIgnoreCase("absent"))
				result[i] = Hint.ABSENT;
			else if (input[i].equalsIgnoreCase("green") || input[i].equalsIgnoreCase("red") || input[i].equalsIgnoreCase("correct"))
				result[i] = Hint.CORRECT;
			else if (input[i].equalsIgnoreCase("yellow") || input[i].equalsIgnoreCase("present"))
				result[i] = Hint.PRESENT;
			else
				throw new IllegalArgumentException(input.toString());
		}
	}

	// Return the values with ANSI colors.
	public String toString() {
		StringBuilder out = new StringBuilder();
		out.append('[');
		for (int i = 0; i < result.length; i++) {
			Report.Hint clue = result[i];
			out.append(COLORMAP.get(clue));
			out.append(clue);
			out.append(ANSI_RESET);
			if (i < result.length - 1)
				out.append(", ");
		}
		out.append(']');
		return out.toString();
	}

	public boolean equals(Report r) {
		return Arrays.equals(this.result, r.result);
	}

	/*
	 * True if all cells are CORRECT
	 */
	public boolean isSolved() {
		for (int i = 0; i < Position.NUMBERCELLS; i++) {
			if (result[i] != Hint.CORRECT)
				return false;
		}
		return true;
	}

}
