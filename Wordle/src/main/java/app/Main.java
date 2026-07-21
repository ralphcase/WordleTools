package app;

import dictionary.DictionaryInitializer;
import dictionary.WordRepository;
import feedback.Feedback;
import feedback.Mark;
import solver.Solver;
import word.Word;

import java.util.List;

/**
 * Solve a wordle game.
 */
public class Main {
    static final Mark green = Mark.CORRECT;
    static final Mark yellow = Mark.PRESENT;
    static final Mark gray = Mark.ABSENT;

    /**
     * Entry point for the WordleTools application.
     * Take input as code changes about what "mode" to use and what hints are shown by the Wordle
     * app at https://www.nytimes.com/games/wordle/index.html.
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        DictionaryInitializer initializer = new DictionaryInitializer();
        WordRepository repo = initializer.loadDictionaries();

        boolean hard = false;
//        Solver solver = new Solver(repo, hard, Solver.Mode.ARCHIVE);
        Solver solver = new Solver(repo, hard, Solver.Mode.NEW);
//        Solver solver = new Solver(repo, hard, Solver.Mode.ALL);

        solver.applyFeedback(new Word("SANER"), Feedback.of(green, gray, gray, gray, gray));
        DictionaryBuilder.predictWordlebot(solver.remainingCandidates());

        solver.applyFeedback(new Word("THIOL"), Feedback.of(gray, green, green, gray, green));
//        solver.applyFeedback(new Word("COUTH"), Feedback.of(gray, green, gray, green, green));
//        solver.applyFeedback(new Word("PUDIC"), Feedback.of(gray, yellow, gray, gray, gray));

        // Print remaining candidates
        List<Word> candidates = solver.remainingCandidates();
        System.out.println(candidates.size() + " Remaining candidates: " + candidates);

//        System.out.println("Next Guess: " + solver.nextGuess());
        System.out.println("Next Guess: " + solver.rankedGuesses(10));

        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
    }
}
