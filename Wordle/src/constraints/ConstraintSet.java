package constraints;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import feedback.Feedback;
import word.Word;

public final class ConstraintSet {

	final Character[] mustBe = new Character[Word.LENGTH];
	@SuppressWarnings("unchecked")
	final Set<Character>[] cannotBe = new Set[Word.LENGTH];
	final Set<Character> mustContain = new HashSet<>();
	final Set<Character> cannotContain = new HashSet<>();
	final Map<Character, Integer> minCount = new HashMap<>();
	final Map<Character, Integer> maxCount = new HashMap<>();

	public ConstraintSet() {
		for (int i = 0; i < Word.LENGTH; i++) {
			cannotBe[i] = new HashSet<Character>();
		}
	}

	public boolean allows(Object candidate) {
		// placeholder implementation
		return true;
	}

	public ConstraintSet updatedBy(Word guess, Feedback fb) {
		for (int i = 0; i < Word.LENGTH; i++) {
			char letter = guess.text().charAt(i);

			switch (fb.marks()[i]) {
			case CORRECT -> exactMatchAtPosition(i, letter);
			case PRESENT -> presentElsewhere(i, letter);
			case ABSENT -> absentFromSolution(i, letter);
			}
		}
		updateCountsFrom(guess, fb);
		checkConsistency();
		return this;
	}

	private void checkConsistency() {

	    // 1. No letter may be both required and forbidden globally
	    for (char letter : mustContain) {
	        if (cannotContain.contains(letter)) {
	            throw new IllegalStateException(
	                "Letter '" + letter + "' is both required and forbidden"
	            );
	        }
	    }

	    // 2. No position may require a letter that is forbidden there
	    for (int i = 0; i < Word.LENGTH; i++) {
	        Character required = mustBe[i];
	        if (required != null && cannotBe[i].contains(required)) {
	            throw new IllegalStateException(
	                "Position " + i + " requires '" + required +
	                "' but it is forbidden there"
	            );
	        }
	    }

	    // 3. minCount(letter) <= maxCount(letter)
	    for (var entry : minCount.entrySet()) {
	        char letter = entry.getKey();
	        int min = entry.getValue();
	        Integer max = maxCount.get(letter);

	        if (max != null && min > max) {
	            throw new IllegalStateException(
	                "minCount(" + letter + ") = " + min +
	                " exceeds maxCount(" + letter + ") = " + max
	            );
	        }
	    }

	    // 4. No letter required at a position may be globally forbidden
	    for (int i = 0; i < Word.LENGTH; i++) {
	        Character required = mustBe[i];
	        if (required != null && cannotContain.contains(required)) {
	            throw new IllegalStateException(
	                "Position " + i + " requires '" + required +
	                "' but it is globally forbidden"
	            );
	        }
	    }

	    // 5. No letter required globally may have maxCount = 0
	    for (char letter : mustContain) {
	        Integer max = maxCount.get(letter);
	        if (max != null && max == 0) {
	            throw new IllegalStateException(
	                "Letter '" + letter + "' is required but maxCount=0"
	            );
	        }
	    }
	}

	private void exactMatchAtPosition(int i, char letter) {

	    // 1. Positional requirement
	    Character existing = mustBe[i];
	    if (existing != null && existing != letter) {
	        throw new IllegalStateException(
	            "Position " + i + " already requires '" + existing +
	            "', cannot also require '" + letter + "'"
	        );
	    }
	    mustBe[i] = letter;

	    // 2. Contradiction: this position previously forbade this letter
	    if (cannotBe[i].contains(letter)) {
	        throw new IllegalStateException(
	            "Cannot require '" + letter + "' at position " + i +
	            " because it was previously forbidden there"
	        );
	    }

	    // 3. Presence requirement
	    if (cannotContain.contains(letter)) {
	        throw new IllegalStateException(
	            "Cannot require '" + letter + "' because it was previously marked absent"
	        );
	    }
	    mustContain.add(letter);
	}
	
	private void presentElsewhere(int i, char letter) {

	    // 1. Positional: cannot be here
	    if (mustBe[i] != null && mustBe[i] == letter) {
	        throw new IllegalStateException(
	            "Cannot mark '" + letter + "' as PRESENT at position " + i +
	            " because it is already required there"
	        );
	    }
	    cannotBe[i].add(letter);

	    // 2. Presence: must appear somewhere
	    if (cannotContain.contains(letter)) {
	        throw new IllegalStateException(
	            "Cannot mark '" + letter + "' as PRESENT because it was previously marked absent"
	        );
	    }
	    mustContain.add(letter);
	}

	private void absentFromSolution(int i, char letter) {

	    // 1. If we already know the letter must appear somewhere,
	    //    then gray here only forbids this position.
	    if (mustContain.contains(letter)) {
	        // But it must not contradict a green at this position
	        if (mustBe[i] != null && mustBe[i] == letter) {
	            throw new IllegalStateException(
	                "Cannot mark '" + letter + "' as ABSENT at position " + i +
	                " because it is already required there"
	            );
	        }
	        cannotBe[i].add(letter);
	        return;
	    }

	    // 2. If the letter is required at this position, contradiction
	    if (mustBe[i] != null && mustBe[i] == letter) {
	        throw new IllegalStateException(
	            "Cannot mark '" + letter + "' as ABSENT at position " + i +
	            " because it is required there"
	        );
	    }

	    // 3. If minCount already requires at least one occurrence, contradiction
	    Integer min = minCount.get(letter);
	    if (min != null && min > 0) {
	        throw new IllegalStateException(
	            "Cannot mark '" + letter + "' as ABSENT because minCount=" + min
	        );
	    }

	    // 4. If the letter was previously required globally, contradiction
	    if (mustContain.contains(letter)) {
	        throw new IllegalStateException(
	            "Cannot mark '" + letter + "' as ABSENT because it was previously required"
	        );
	    }

	    // 5. Now we can forbid the letter globally
	    cannotContain.add(letter);

	    // 6. And forbid it at all positions
	    for (int pos = 0; pos < Word.LENGTH; pos++) {
	        cannotBe[pos].add(letter);
	    }

	    // 7. We do NOT set maxCount here.
	    //    That will be handled in the count-aggregation step.
	}

	private void updateCountsFrom(Word guess, Feedback fb) {

	    Map<Character, Integer> presentCount = new HashMap<>(); // CORRECT or PRESENT
	    Set<Character> absent = new HashSet<>();                // letters with at least one ABSENT mark

	    for (int i = 0; i < Word.LENGTH; i++) {
	        char letter = guess.text().charAt(i);

	        switch (fb.marks()[i]) {
	            case CORRECT, PRESENT ->
	                presentCount.merge(letter, 1, Integer::sum);

	            case ABSENT ->
	                absent.add(letter);
	        }
	    }

	    // 1. Update minCount: at least the number of green/yellow occurrences
	    for (var entry : presentCount.entrySet()) {
	        char letter = entry.getKey();
	        int count = entry.getValue();
	        minCount.merge(letter, count, Math::max);
	    }

	    // 2. Update maxCount
	    for (char letter : absent) {
	        int present = presentCount.getOrDefault(letter, 0);

	        if (present == 0) {
	            // All occurrences were gray so letter is absent from solution
	            maxCount.put(letter, 0);
	        } else {
	            // Mixed marks so letter appears exactly 'present' times
	            maxCount.merge(letter, present, Math::min);
	        }
	    }
	}
}
