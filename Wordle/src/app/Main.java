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
        // Load dictionary
        DictionaryInitializer initializer = new DictionaryInitializer();
        WordRepository repo = initializer.loadDictionaries();

        // Create solver
        Solver solver = new Solver(repo);

        // Hardcoded example guess + feedback
        Word guess = new Word("CRANE");
        Feedback fb = Feedback.of(ABSENT, PRESENT, ABSENT, ABSENT, ABSENT);

        solver.applyFeedback(guess, fb);

        // Print remaining candidates
        List<Word> cands = solver.remainingCandidates();
        System.out.println(cands.size() +" Remaining candidates: "+cands);
    }
}
