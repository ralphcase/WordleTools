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


        // Hardcoded example guess + feedback

        solver.applyFeedback(new Word("OATER"), Feedback.of(gray, gray, gray, yellow, yellow));
        solver.applyFeedback(new Word("DEICE"), Feedback.of(gray, yellow, gray, green, gray));
//        solver.applyFeedback(new Word("FYKED"), Feedback.of(gray, yellow, gray, gray, yellow));
//        solver.applyFeedback(new Word("BEGUM"), Feedback.of(gray, yellow, gray, gray, yellow));
//        solver.applyFeedback(new Word("CLOWN"), Feedback.of(green, green, green, gray, gray ));
//        solver.applyFeedback(new Word("WISER"), Feedback.of(ABSENT, CORRECT, CORRECT, CORRECT, CORRECT));

        // Print remaining candidates
        List<Word> candidates = solver.remainingCandidates();
        System.out.println(candidates.size() + " Remaining candidates: " + candidates);

        System.out.println("Next Guess: " + solver.nextGuess());

        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");

    }
}
