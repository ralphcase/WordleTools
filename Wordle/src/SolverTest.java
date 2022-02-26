import static org.junit.Assert.*;

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
		Assert.assertEquals(120, Solver.countPossible(possible, guesses));
		
		guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("RAISE"), new Report(new ArrayList<String>(List.of("gray", "gray", "gray", "gray", "gray")))));
		guesses.add(new Guess(new Position("BAYOU"), new Report(new ArrayList<String>(List.of("gray", "gray", "green", "present", "gray")))));
		Assert.assertEquals(1, Solver.countPossible(possible, guesses));
		
		guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("RAISE"), new Report(new ArrayList<String>(List.of("gray", "gray", "yellow", "gray", "gray")))));
		guesses.add(new Guess(new Position("MINTY"), new Report(new ArrayList<String>(List.of("gray", "green", "gray", "gray", "gray")))));
		Assert.assertEquals(8, Solver.countPossible(possible, guesses));

		guesses = new ArrayList<Guess>();
		guesses.add(new Guess(new Position("RAISE"), new Report(new ArrayList<String>(List.of("gray", "gray", "gray", "yellow", "gray")))));
		guesses.add(new Guess(new Position("BORTZ"), new Report(new ArrayList<String>(List.of("gray", "green", "gray", "green", "gray")))));
		Assert.assertEquals(3, Solver.countPossible(possible, guesses));
		
	}

	
	
}
