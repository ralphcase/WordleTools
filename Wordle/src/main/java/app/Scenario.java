package app;

import dictionary.DictionaryInitializer;
import dictionary.WordRepository;
import solver.Solver;
import word.Word;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Scenario {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        DictionaryInitializer initializer = new DictionaryInitializer();
        WordRepository repo = initializer.loadDictionaries();

        boolean hard = false;
//        Solver solver = new Solver(repo, hard, Solver.Mode.ARCHIVE);
//        Solver solver = new Solver(repo, hard, Solver.Mode.NEW);
        Solver solver = new Solver(repo, hard, Solver.Mode.ALL);

        List<Word> goals = repo.goalWords();
        Word target = goals.get(ThreadLocalRandom.current().nextInt(goals.size()));

        ScenarioTester st = new ScenarioTester(solver, target);
        st.run();


        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");

    }

}
