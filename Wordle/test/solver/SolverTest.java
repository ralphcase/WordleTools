package solver;

import org.junit.jupiter.api.Test;

import dictionary.WordRepository;
import feedback.Feedback;
import feedback.Mark;
import word.Word;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class SolverTest {
	@Test
	void remainingCandidatesRespectsConstraints() {
		WordRepository repo = new WordRepository(
				List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK")),
				List.of(new Word("CRANE"), new Word("SLATE"), new Word("BRICK")), 
				List.of());

		Solver solver = new Solver(repo);

		Word guess = new Word("CRANE");

		Feedback fb = Feedback.of(
				Mark.ABSENT, // C
				Mark.PRESENT, // R
				Mark.ABSENT, // A
				Mark.ABSENT, // N
				Mark.ABSENT // E
		);

		solver.applyFeedback(guess, fb);

		List<Word> candidates = solver.remainingCandidates();

		assertTrue(candidates.isEmpty());
		}

}
