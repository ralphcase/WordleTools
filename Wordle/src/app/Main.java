package app;

import dictionary.DictionaryInitializer;
import dictionary.WordRepository;
import feedback.Feedback;
import static feedback.Mark.*;

import java.util.List;

import solver.Solver;
import word.Word;

public class Main {

    public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		

        // Load dictionary
        DictionaryInitializer initializer = new DictionaryInitializer();
        WordRepository repo = initializer.loadDictionaries();

        // Create solver
        Solver solver = new Solver(repo);

        // Hardcoded example guess + feedback

        solver.applyFeedback(new Word("OATER"), Feedback.of(ABSENT, ABSENT, ABSENT, CORRECT, CORRECT));
//        solver.applyFeedback(new Word("BLIND"), Feedback.of(ABSENT, ABSENT, PRESENT, ABSENT, ABSENT));
//        solver.applyFeedback(new Word("PHASM"), Feedback.of(ABSENT, ABSENT, ABSENT, PRESENT, ABSENT));
//        solver.applyFeedback(new Word("WISER"), Feedback.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT));

        // Print remaining candidates
        List<Word> cands = solver.remainingCandidates();
        System.out.println(cands.size() +" Remaining candidates: "+cands);
        
        System.out.println("Next Guess: "+ solver.nextGuess());
        
		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");

    }
}
