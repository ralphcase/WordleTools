package app;

import dictionary.DictionaryInitializer;
import dictionary.WordRepository;
import feedback.Feedback;
import feedback.Mark;
import static feedback.Mark.*;

import java.util.List;

import solver.Solver;
import word.Word;

public class Main {
	static final Mark green = Mark.CORRECT;
	static final Mark yellow = Mark.PRESENT;
	static final Mark gray = Mark.ABSENT;

    public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		

        // Load dictionary
        DictionaryInitializer initializer = new DictionaryInitializer();
        WordRepository repo = initializer.loadDictionaries();

        // Create solver
        Solver solver = new Solver(repo);

        // Hardcoded example guess + feedback

        solver.applyFeedback(new Word("OATER"), Feedback.of(gray, gray, gray, gray, gray));
//        solver.applyFeedback(new Word("CUSPY"), Feedback.of(yellow, gray, gray, gray, gray));
//        solver.applyFeedback(new Word("HWYLS"), Feedback.of(yellow, gray, gray, yellow, gray));
        solver.applyFeedback(new Word("WISER"), Feedback.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT));

        // Print remaining candidates
        List<Word> cands = solver.remainingCandidates();
        System.out.println(cands.size() +" Remaining candidates: "+cands);
        
        System.out.println("Next Guess: "+ solver.nextGuess());
        
		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");

    }
}
