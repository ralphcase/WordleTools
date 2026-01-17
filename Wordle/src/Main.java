
import dictionary.WordListLoader;
import dictionary.WordRepository;
import position.Position;
import report.Report;
import solver.Solver;
import solver.Guess;

import java.util.List;

public final class Main {

    public static void main(String[] args) {
		long startTime = System.currentTimeMillis();

//    	basic();
		solver();
    	
		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
    }
    
    private static void solver() {
        WordListLoader loader = new WordListLoader();

        List<Position> goals = loader.loadPositions("possibleGoals.txt");
        List<Position> allowed = loader.loadPositions("nonGoals.txt");
        List<Position> solutions = loader.loadPositions("solutions.txt");

        WordRepository repo = new WordRepository(goals, allowed, solutions);


    	
    }
    
    private static void basic() {

        // 1. Load dictionaries
        WordListLoader loader = new WordListLoader();

        List<Position> goals = loader.loadPositions("possibleGoals.txt");
        List<Position> allowed = loader.loadPositions("nonGoals.txt");
        List<Position> solutions = loader.loadPositions("solutions.txt");

        WordRepository repo = new WordRepository(goals, allowed, solutions);

        // 2. Create solver
        Solver solver = new Solver(repo);

        // 3. Choose a target (for now, just pick the first goal)
        Position target = repo.getGoalWords().get(0);
        System.out.println("Target word: " + target);

        // 4. Solve loop
        int turn = 1;
        while (true) {

            // Choose next guess
            Position guessPos = solver.chooseNextGuess();
            Report report = new Report(target, guessPos);
            Guess guess = new Guess(guessPos, report);

            System.out.println("Turn " + turn + ": " + guess);

            // Check if solved
            if (report.isSolved()) {
                System.out.println("Solved in " + turn + " turns!");
                break;
            }

            // Apply feedback to narrow candidates
            solver.applyFeedback(guess.getPosition(), guess.getReport());

            turn++;

            if (solver.getCandidates().isEmpty()) {
                System.out.println("No candidates left — inconsistent feedback?");
                break;
            }
        }
    }
}