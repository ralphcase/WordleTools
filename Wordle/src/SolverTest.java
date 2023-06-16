import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class SolverTest {

	@Test
	public void testCountPossible() {
		List<Position> possible = Position.getGOALWORDS();
		ArrayList<Guess> guesses = new ArrayList<Guess>();
		
		guesses.add(new Guess(new Position("RAISE"), new Report(new ArrayList<String>(List.of("gray", "gray", "gray", "gray", "yellow")))));
		System.out.println(Solver.countPossible(possible, guesses));
		Assert.assertEquals(238, Solver.countPossible(possible, guesses));
		
		guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("RAISE"), new Report(new ArrayList<String>(List.of("gray", "gray", "gray", "gray", "gray")))));
		guesses.add(new Guess(new Position("BAYOU"), new Report(new ArrayList<String>(List.of("gray", "gray", "green", "present", "gray")))));
		Assert.assertEquals(1, Solver.countPossible(possible, guesses));
		
		guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("RAISE"), new Report(new ArrayList<String>(List.of("gray", "gray", "yellow", "gray", "gray")))));
		guesses.add(new Guess(new Position("MINTY"), new Report(new ArrayList<String>(List.of("gray", "green", "gray", "gray", "gray")))));
		Assert.assertEquals(12, Solver.countPossible(possible, guesses));

		guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("TASTE"), new Report(new ArrayList<String>(List.of("green", "green", "green", "green", "green")))));
		Assert.assertEquals(1, Solver.countPossible(possible, guesses));

		guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("TASTE"), new Report(new ArrayList<String>(List.of("green", "green", "green", "green", "green")))));
		guesses.add(new Guess(new Position("AUDIO"), new Report(new ArrayList<String>(List.of("green", "green", "green", "green", "green")))));
		Assert.assertEquals(0, Solver.countPossible(possible, guesses));

	}
	
	@Test
	public void testPossibleSize() {
		List<Position> possible = new ArrayList<Position>();
		possible.add(new Position("TASTE"));
		ArrayList<Guess> guesses = new ArrayList<Guess>();
		Assert.assertEquals(1, Solver.possibleSize(possible, guesses, new Guess(new Position("TASTE"), new Position("TASTE"))));

		guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("TASTE"), new Report(new ArrayList<String>(List.of("green", "green", "green", "green", "green")))));
		Assert.assertEquals(0, Solver.possibleSize(possible, guesses, new Guess(new Position("LEAST"), new Report(new ArrayList<String>(List.of("green", "green", "green", "green", "green"))))));

	}

	@Test
	public void testBestTurn() {
		List<Position> possible = new ArrayList<Position>();
		possible.add(new Position("TASTE"));
		ArrayList<Guess> guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("TASTE"), new Report(new ArrayList<String>(List.of("green", "green", "green", "green", "green")))));
		Assert.assertTrue(Solver.bestTurn(possible, guesses, possible).equals(new Position("TASTE")));
		
		List<Position> allWords = Position.getALLWORDS();
		Assert.assertTrue(Solver.bestTurn(possible, guesses, allWords).equals(new Position("TASTE")));
		
	}
	
	
}
