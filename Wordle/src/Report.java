import java.util.Arrays;
import java.util.List;
import java.util.Map;

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
			Hint.ABSENT, 	ANSI_GRAY, 
			Hint.PRESENT, 	ANSI_YELLOW, 
			Hint.CORRECT, 	ANSI_GREEN
			);

	private Hint[] result;
	
	public Report(Position target, Position guess) {
		result = new Hint[Position.NUMBERCELLS];
		boolean[] matched = new boolean[Position.NUMBERCELLS];
		exactMatches(target, guess, matched);
		presentLetters(target, guess, matched);
		absentLetters();
	}

	private void exactMatches(Position target, Position guess, boolean[] matched) {
		for (int i = 0; i < Position.NUMBERCELLS; i++) {
			if (target.toCharArray()[i] == guess.toCharArray()[i]) {
				result[i] = Hint.CORRECT;
				matched[i] = true;
			}
		}
	}

	private void presentLetters(Position target, Position guess, boolean[] matched) {
		for (int i = 0; i < Position.NUMBERCELLS; i++) {
			for (int j = 0; j < Position.NUMBERCELLS; j++) {
				if (!matched[j] 
						&& result[i] != Hint.CORRECT 
						&& i != j
						&& target.toCharArray()[j] == guess.toCharArray()[i]) {
					result[i] = Hint.PRESENT;
					matched[j] = true;
					break;
				}
			}
		}
	}

	private void absentLetters() {
		for (int i = 0; i < Position.NUMBERCELLS; i++) {
			if (result[i] == null) {
				result[i] = Hint.ABSENT;
			}
		}
	}

	public Report(String[] input) {
		fillResult(input);
	}

	public Report(List<String> input) {
		fillResult(input.toArray(new String[0]));
	}

	private void fillResult(String[] input) {
		result = new Hint[Position.NUMBERCELLS];
		for (int i = 0; i < Position.NUMBERCELLS; i++) {
			switch (input[i].toLowerCase()) {
			case "gray":
			case "absent":
				result[i] = Hint.ABSENT;
				break;
			case "green":
	        case "red":
	        case "correct":
	            result[i] = Hint.CORRECT;
	            break;
	        case "yellow":
	        case "present":
	            result[i] = Hint.PRESENT;
	            break;
	        default:
	            throw new IllegalArgumentException(input.toString());
			}
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		Report report = (Report) obj;
		return Arrays.equals(result, report.result);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(result);
	}

	/*
	 * True if all cells are CORRECT
	 */
	public boolean isSolved() {
		for (Hint hint : result) {
			if (hint != Hint.CORRECT) {
				return false;
			}
		}
		return true;
	}

}
