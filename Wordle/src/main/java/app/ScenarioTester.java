package app;

import feedback.Feedback;
import solver.Solver;
import word.Word;

public class ScenarioTester {

    private final Solver solver;
    private final Word goal;

    public ScenarioTester(Solver solver, Word goal) {
        this.solver = solver;
        this.goal = goal;
    }

    public void run() {
        int step = 1;

        while (true) {
            Word guess = solver.nextGuess();
            Feedback report = Feedback.from(guess, goal);

            System.out.printf("%d. %s → %s%n", step, guess, report);

            if (report.isSolved()) {
                System.out.println("Solved in " + step + " steps");
                return;
            }

            solver.applyFeedback(guess, report);
            step++;
        }
    }
}
