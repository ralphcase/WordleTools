package app;

import dictionary.DictionaryInitializer;
import dictionary.WordRepository;
import solver.Solver;
import word.Word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Scenario {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        DictionaryInitializer initializer = new DictionaryInitializer();
        WordRepository repo = initializer.loadDictionaries();

        boolean hard = false;
//        Solver solver = new Solver(repo, hard, Solver.Mode.ARCHIVE);
//        Solver solver = new Solver(repo, hard, Solver.Mode.NEW);
//        Solver solver = new Solver(repo, hard, Solver.Mode.ALL);

//        List<Word> goals = repo.goalWords();
        List<Word> goals = new ArrayList<Word>(repo.goalWords());
        goals.removeAll(repo.pastSolutionWords());
//        Word target = goals.get(ThreadLocalRandom.current().nextInt(goals.size()));
        int[] histogram = new int[10];
//        for (Word target : goals.subList(0, 50)) {
        for (Word target : goals) {
            System.out.println("Target: " + target);

            ScenarioTester st = new ScenarioTester(new Solver(repo, hard, Solver.Mode.NEW), target);
            int num = st.run();
            histogram[num]++;
        }
        System.out.println("Histogram of number of guesses:" + Arrays.toString(histogram));

        long endTime = System.currentTimeMillis();
        System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
    }
}