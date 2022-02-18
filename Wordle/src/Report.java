import java.util.Arrays;
import java.util.List;

public class Report {
	private Hint[] result = new Hint[Position.NUMBERCELLS];

	public Report(Position target, Position guess) {
		boolean[] matched = new boolean[Position.NUMBERCELLS];
		// Check for exact matches.
		for (int i = 0; i < Position.NUMBERCELLS; i++)
			if (target.getPos().charAt(i) == guess.getPos().charAt(i)) {
				result[i] = Hint.CORRECT;
				matched[i] = true;
			}
		
		for (int i = 0; i < Position.NUMBERCELLS; i++)
			// loop through letters in the guess.
			for (int j = 0; j < Position.NUMBERCELLS; j++)
				// loop through unmatched letters in the target.
				if (!matched[j] 
						&& result[i] != Hint.CORRECT
						&& i != j 
						&& target.getPos().charAt(j) == guess.getPos().charAt(i)) {
					result[i] = Hint.PRESENT;
					matched[j] = true;
					break;
				}
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
			else if (input[i].equalsIgnoreCase("green") || input[i].equalsIgnoreCase("correct"))
				result[i] = Hint.CORRECT;
			else if (input[i].equalsIgnoreCase("yellow") || input[i].equalsIgnoreCase("present"))
				result[i] = Hint.PRESENT;
			else
				throw new IllegalArgumentException(input.toString());
		}
	}

	public String toString() {
		return Arrays.toString(result);
	}

	public boolean equals(Report r) {
		for (int i = 0; i < result.length; i++)
			if (this.result[i] != r.result[i]) 
				return false;
		return true;
	}

	public boolean isSolved() {
		for (int i = 0; i < Position.NUMBERCELLS; i++) {
			if (result[i] != Hint.CORRECT)
				return false;
		}
		return true;
	}

}
