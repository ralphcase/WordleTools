package app;

import feedback.Feedback;
import solver.Solver;
import word.Word;

/**
 * Set up and test a given Solver finding a goal word.
 */
public class ScenarioTester {

  private final Solver solver;
  private final Word goal;

  public ScenarioTester(Solver solver, Word goal) {
    this.solver = solver;
    this.goal = goal;
  }

  /*
  Use the Solver to find the goal.
  Return the number of guesses.
   */
  public int run() {
    int step = 1;

    Word prevGuess = null;
    while (true) {
      Word guess = solver.nextGuess();
      if (prevGuess != null && prevGuess.equals(guess)) {
        throw new RuntimeException("Duplicate guess detected. Solver seems stuck.");
      }
      Feedback report = Feedback.from(guess, goal);

      System.out.printf("%d. %s → %s%n", step, guess, report);

      if (report.isSolved()) {
        System.out.println("Solved in " + step + " steps");
        return step;
      }

      prevGuess = guess;
      solver.applyFeedback(guess, report);
      step++;
    }
  }
}
