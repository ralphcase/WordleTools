import java.util.ArrayList;
import java.util.List;

public class Solver {
	static List<Position> allWords;
	static List<Position> possible;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		solveHelper();
//		example();
		
		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
	}
	
	private static void example()
	{
		Position turn;
		Position goal = new Position("SHADE");
//		Position goal = new Position("HATCH");    // Hard case for first solver.
		allWords = Position.getALLWORDS();
		possible = Position.getGOALWORDS();
//		System.out.println(possible.size() + " possible: \t" + possible);
//		
//		List<Position> noDoubles = Position.RemoveDouble(possible);
//		System.out.println("reduced "+possible.size()+ " words to "+noDoubles.size()+" without multiple letters.");
//		System.out.println(noDoubles.size() + " possible: \t" + noDoubles);
//		
//		List<Position> noAnas = Position.RemoveAnagrams(noDoubles);
//		System.out.println("reduced "+possible.size()+ " words to "+noAnas.size()+" without anagrams.");
//		System.out.println(noAnas.size() + " possible: \t" + noAnas);
//			
		ArrayList<Guess> guesses = new ArrayList<Guess>();
		//		
		System.out.println("Goal: " + goal);
		turn = new Position("RAISE");		
		Guess g = new Guess(turn, goal.guess(turn));
		guesses.add(g);
		
		while (guesses.size() == 0 || !guesses.get(guesses.size()-1).isSolved()) {
			System.out.print("["+guesses.size()+"] "+possible.size() + " possible: \t");
			if (possible.size() < 100)
				System.out.print(possible);
			System.out.println();
			// Shorten the list of possibilities based on the new results.
			removeImpossible(possible, guesses);

			// Choose a guess for this turn.
			turn = worstTurn(possible, guesses, allWords);
//			turn = deepBestTurn(possible, guesses, allWords);
			guesses.add(new Guess(turn, goal.guess(turn)));
//			System.out.println("Guesses: " + guesses);
		}
		System.out.println("["+guesses.size()+"] "+possible.size() + " possible: \t" + possible);
		System.out.println("Guesses: " + guesses);
		System.out.println("Solved in "+guesses.size()+" turns! ");
	}
	
	private static void solveHelper() {
		allWords = Position.getALLWORDS();
		possible = Position.getGOALWORDS();
//		possible.addAll(allWords);
		ArrayList<Guess> guesses = new ArrayList<Guess>();
		
		
		guesses.add(new Guess(new Position("MANIA"), new Report(new ArrayList<String>(List.of("gray", "gray", "gray", "gray", "gray")))));
		guesses.add(new Guess(new Position("BUSED"), new Report(new ArrayList<String>(List.of("gray", "gray", "gray", "yellow", "gray")))));
		guesses.add(new Guess(new Position("GROVE"), new Report(new ArrayList<String>(List.of("green", "gray", "green", "green", "green")))));
////		guesses.add(new Guess(new Position("WOOLY"), new Report(new ArrayList<String>(List.of("yellow", "green", "gray", "green", "green")))));
//		guesses.add(new Guess(new Position("POUND"), new Report(new ArrayList<String>(List.of("gray", "green", "green", "green", "green")))));
//		guesses.add(new Guess(new Position("MOUND"), new Report(new ArrayList<String>(List.of("gray", "green", "green", "green", "green")))));
////		guesses.add(new Guess(new Position("QUERY"), new Report(new ArrayList<String>(List.of("gray", "gray", "yellow", "gray", "gray")))));
//		guesses.add(new Guess(new Position("DEBAR"), new Report(new ArrayList<String>(List.of("green", "green", "gray", "gray", "gray")))));
				
		removeImpossible(possible, guesses);
		System.out.println("["+guesses.size()+"] "+possible.size() + " possible: \t" + possible);
		System.out.println("Guesses: " + guesses);
		System.out.println("best: "+bestTurn(possible, guesses, allWords));	
		System.out.println("worst: "+worstTurn(possible, guesses, allWords));	
	}

	
	/*
	 * Given a list of possible Positions and a list of guesses already made,
	 * return the best next guess. "Best" is the guess that would reduce the
	 * number of possibilities the most.
	 */
	private static Position bestTurn(List<Position> possible, List<Guess> guesses, List<Position> trialList) {
		
		// For each possible next move, see which reduces the possible list the
		// most on average.
		int minTotal = Integer.MAX_VALUE;
		Position best = possible.get(0);
		for (Position pos : possible) {

				int total = 0;
				for (Position trial : trialList) {
//					System.out.println("best: "+best+", checking "+pos+" with "+trial);
					total += possibleSize(possible, guesses, new Guess(pos, trial.guess(pos)));
					if (total >= minTotal)
						break;
				}
//				System.out.println("for guess "+pos+", the size is "+total);
				if (total < minTotal) {
					// Save the new best.
					minTotal = total;
					best = pos;

			}
//			System.out.println(count + ": " + found + " " + minTotal + " " + best + " - " + total + " " + pos);
//			System.out.println(count + ": " + minTotal + " " + best + " - " + total + " " + pos);
		}
		return best;
	}
	
	
	/*
	 * Given a list of possible Positions and a list of guesses already made,
	 * return the worst next guess. "Worst" is the guess that would reduce the
	 * number of possibilities the least.
	 */
	private static Position worstTurn(List<Position> possible, List<Guess> guesses, List<Position> trialList) {
		
		int maxTotal = 0;
		Position worst = possible.get(0);
		for (Position pos : possible) {

				int total = 0;
				for (Position trial : trialList) {
					total += possibleSize(possible, guesses, new Guess(pos, trial.guess(pos)));
					if (total <= maxTotal)
						break;
				}
				if (total > maxTotal) {
					// Save the new best.
					maxTotal = total;
					worst = pos;

			}
		}
		return worst;
	}
	
	
	private static Position deepBestTurn(List<Position> possible, List<Guess> guesses, List<Position> trialList) {
		
		// For each possible next move, see which reduces the possible list the
		// most on average.
		if (trialList.size() == 1) return trialList.get(0);
		
		int minTotal = Integer.MAX_VALUE;
		Position best = possible.get(0);
		for (Position pos : possible) {

				int total = 0;
				for (Position trial : trialList) {
//					System.out.println("best: "+best+", checking "+pos+" with "+trial);
					total += deepPossibleSize(possible, guesses, new Guess(pos, trial.guess(pos)));
					if (total >= minTotal)
						break;
				}

				if (total < minTotal) {
					// Save the new best.
					minTotal = total;
					best = pos;

			}
//			System.out.println(count + ": " + found + " " + minTotal + " " + best + " - " + total + " " + pos);
//			System.out.println(count + ": " + minTotal + " " + best + " - " + total + " " + pos);
		}
		return best;
	}

	
	private static int deepPossibleSize(List<Position> possible, List<Guess> guesses, Guess nextGuess) {
		guesses.add(nextGuess);
		int count = countPossible(possible, guesses); 
		if (count > 1) {
			List<Position> thisPossible = new ArrayList<Position>(possible);
			removeImpossible(thisPossible, guesses);
			int minTotal = Integer.MAX_VALUE;
			int total = 0;
			for (Position pos: thisPossible) {
				total = deepPossibleSize(thisPossible, guesses, new Guess(pos, deepBestTurn(thisPossible, guesses, thisPossible).guess(pos)));
				if (total < minTotal) {
					minTotal = total;
				}
			count = minTotal;	
			}
		}
		guesses.remove(nextGuess);
		return count;
	}

	/*
	 * Return the size of possible positions given the previous guesses and the
	 * next potential guess.
	 */
	private static int possibleSize(List<Position> possible, List<Guess> guesses, Guess nextGuess) {
		guesses.add(nextGuess);
		int count = countPossible(possible, guesses);
		guesses.remove(nextGuess);
		return count;
	}

	/*
	 * Remove Positions from the list if they are impossible given the data in
	 * the guesses.
	 */
	private static void removeImpossible(List<Position> possible, List<Guess> allGuesses) {
		for (int i = 0; i < possible.size(); i++) {
			for (Guess g : allGuesses) {
				Report hint = possible.get(i).guess(g.getPos());
				if (!hint.equals(g.getScore())) {
					possible.remove(i);
					i--;
					break;
				}
			}
		}
	}
	
	/*
	 * Count the number of positions that are consistent with all the guesses.
	 */
	static int countPossible(List<Position> possible, List<Guess> allGuesses) {
		int count = 0;
		for (Position p: possible) {
			for (Guess g : allGuesses) {
				Report hint = p.guess(g.getPos());
				if (!hint.equals(g.getScore())) {
					count++;
					break;
				}
			}
		}
		return possible.size()-count;
	}
}