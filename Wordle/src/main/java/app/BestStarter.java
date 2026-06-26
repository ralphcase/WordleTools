package app;

import dictionary.DictionaryInitializer;
import dictionary.WordRepository;
import solver.Solver;

/**
 * Try all starting words to project which is the best for a given scenario.
 */
public class BestStarter {

  /**
   * Find the best starting guesses and report how long it took.
   *
   * @param args not used
   */
  public static void main(String[] args) {
    long startTime = System.currentTimeMillis();

    DictionaryInitializer initializer = new DictionaryInitializer();
    WordRepository repo = initializer.loadDictionaries();

    boolean hard = false;
    Solver solver = new Solver(repo, hard, Solver.Mode.ARCHIVE);    // IRATE
//    Solver solver = new Solver(repo, hard, Solver.Mode.ALL);    // RAISE
//    Solver solver = new Solver(repo, hard, Solver.Mode.NEW);  // SANER

    System.out.println("Best Starting guess: " + solver.rankedGuesses(100));

    long endTime = System.currentTimeMillis();
    System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
  }
}
