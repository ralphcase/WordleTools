package app;

import dictionary.DictionaryInitializer;
import dictionary.WordRepository;

import solver.Solver;

public class BestStarter {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        DictionaryInitializer initializer = new DictionaryInitializer();
        WordRepository repo = initializer.loadDictionaries();

        Solver solver = new Solver(repo);

        System.out.println("Best Starting guess: " + solver.nextGuess());

        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
    }
}
