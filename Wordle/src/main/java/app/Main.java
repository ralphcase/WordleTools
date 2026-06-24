package app;

import dictionary.DictionaryInitializer;
import dictionary.WordRepository;
import feedback.Feedback;
import feedback.Mark;
import solver.Solver;
import word.Word;

import java.util.List;

public class Main {
    static final Mark green = Mark.CORRECT;
    static final Mark yellow = Mark.PRESENT;
    static final Mark gray = Mark.ABSENT;

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        DictionaryInitializer initializer = new DictionaryInitializer();
        WordRepository repo = initializer.loadDictionaries();

        boolean hard = false;
//        Solver solver = new Solver(repo, hard, Solver.Mode.ARCHIVE);
        Solver solver = new Solver(repo, hard, Solver.Mode.NEW);
//        Solver solver = new Solver(repo, hard, Solver.Mode.ALL);

        solver.applyFeedback(new Word("SANER"), Feedback.of(gray, gray, gray, green, green));
        solver.applyFeedback(new Word("DOILT"), Feedback.of(gray, gray, gray, gray, gray));
        solver.applyFeedback(new Word("CURER"), Feedback.of(gray, green, gray, green, green));
//        solver.applyFeedback(new Word("FREAK"), Feedback.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT));

        // Print remaining candidates
        List<Word> candidates = solver.remainingCandidates();
        System.out.println(candidates.size() + " Remaining candidates: " + candidates);

//        System.out.println("Next Guess: " + solver.nextGuess());
        System.out.println("Next Guess: " + solver.rankedGuesses(10));

        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");

    }
}
