import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/*
 * References:
 * https://sonorouschocolate.com/notes/index.php?title=The_best_strategies_for_Wordle
 * https://www.youtube.com/watch?v=v68zYyaEmEA
 * https://www.nytimes.com/interactive/2022/upshot/wordle-bot.html
 */

public class Solver {
	
	static Logger logger = Logger.getLogger(Solver.class.getName());

//	best: [[OATER:110080], [ARIEL:110704], [RAISE:112784], [RAILE:113122], [ROATE:113164], [TALER:116398], [LARES:116856], [LASER:117000], [SOARE:117476], [RALES:117720], [LATER:118459], [PAREO:118674], [RATEL:118730], [SOREL:118876], [ARLES:118944], [SANER:119319], [SATER:119378], [AROSE:119605], [SOLER:119654], [RAINE:120164], [ALTER:120436], [ARTEL:120562], [TIARE:121920], [TARES:122694], [NAIEO:122838], [ORATE:123150], [TASER:123151], [ORIEL:123460], [ARISE:123783], [OARED:123837], [REALO:123894], [AUREI:124348], [AESIR:124490], [RATES:124710], [ALOES:124814], [SAYER:124850], [SARED:124860], [SERIA:124976], [AEROS:125158], [LOSER:125212], [NARES:125396], [RANES:125596], [OILER:125899], [LORES:126638], [PAIRE:126822], [DEAIR:127054], [ROLES:127074], [URAEI:127646], [IRATE:127820], [AIRED:127831]]
//			[LATER:118459]
//			[SANER:119319]
//			[AROSE:119605]
//			[TASER:123151]
//			[ARISE:123783]
//			[OARED:123837]
//			[OILER:125899]
//			[AIRED:127831]
					
	static String[] starting = {
			"ROATE", "RAISE", "IRATE", "ARISE", "STARE", "ATONE",
			"CRANE", "SLATE", "TRAIN", "ADIEU", "AUDIO", "HOUSE", 
			"GREAT", "HEART", "AROSE", "STEAM", "TEARS", "AISLE",
			"DREAM", "LEAST", "TRACE", "CRATE", "SALET", "SAUCE",
			"OATER", "ARIEL", "LATER", "SANER", "TASER"
	};

// Do we need to get total counts for the number of possible words?
// A value of false allows short-circuiting.
private static boolean countsNeeded = true;

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		
		solveHelper();
//		example();
		
		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
	}
	
	
	private static void solveHelper() {
		List<Position> allWords = Position.getALLWORDS();
		List<Position> possible = Position.getGOALWORDS();
		List<Position> solutions = Position.getSOLUTIONWORDS();
		ArrayList<Guess> guesses = new ArrayList<Guess>();
		
		// Assume that previous solutions are not possible solutions.
//		possible.removeAll	(solutions);
		System.out.println(possible.size() + " possible: \t" + possible);
				
		boolean hardMode = 
//				true;
				false;
		
		guesses.add(new Guess(new Position("OATER"), new Report(new ArrayList<String>(List.of("gray", "yellow", "gray", "gray", "gray")))));
//		guesses.add(new Guess(new Position("SLAIN"), new Report(new ArrayList<String>(List.of("gray", "gray", "yellow", "green", "gray")))));
//		guesses.add(new Guess(new Position("HYNDE"), new Report(new ArrayList<String>(List.of("yellow", "gray", "gray", "gray", "green")))));
//		guesses.add(new Guess(new Position("CLADE"), new Report(new ArrayList<String>(List.of("gray", "green", "green", "green", "green")))));
//		guesses.add(new Guess(new Position("QUICH"), new Report(new ArrayList<String>(List.of("gray", "gray", "gray", "green", "yellow")))));
//		guesses.add(new Guess(new Position("RUBIN"), new Report(new ArrayList<String>(List.of("yellow", "gray", "green", "yellow", "gray")))));

//		guesses.add(new Guess(new Position("FUZZY"), new Report(new ArrayList<String>(List.of("gray", "gray", "gray", "gray", "gray")))));
				
		removeImpossible(possible, guesses);
		System.out.println("[" + guesses.size() + "] " + possible.size() + " possible: \t" + possible);
		System.out.println("Guesses: " + guesses);
		Position best;
		if (hardMode)
			best = bestTurn(possible, guesses, possible);
		else
			best = bestTurn(possible, guesses, allWords);
		System.out.println("\nbest: "+best);	
	}

	
	private static void example()
	{
//		Position goal = new Position("SHADE");
//		Position goal = new Position("HATCH");    // Hard case for first solver.
//		Position goal = new Position("TASTE");    // Hard case for first solver.
		Position goal = new Position();			// Random goal word.
		List<Position> allWords = Position.getALLWORDS();
		List<Position> possible = Position.getGOALWORDS();
//		System.out.println(possible.size() + " possible: \t" + possible);
//		
// 		Prune the list of words, removing those unlikely to help much.
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
	
		System.out.println("Goal: " + goal);
//		turn = new Position("ROATE");	// 185459	
//		guesses.add(new Guess(goal, turn));
		
		while (guesses.size() == 0 || !guesses.get(guesses.size()-1).isSolved()) {
			System.out.println("Guesses: " + guesses);

			// Shorten the list of possibilities based on the new results.
			removeImpossible(possible, guesses);
			System.out.print("["+guesses.size()+"] "+possible.size() + " possible: \t");
			if (possible.size() < 100)
				System.out.println(possible);
			

			// Choose a guess for this turn.
			Position turn = bestTurn(possible, guesses, allWords);
			guesses.add(new Guess(goal, turn));
		}
		System.out.println("Guesses: " + guesses);
		System.out.println("Solved in " + guesses.size() + " turns! ");
	}
	
	
	/*
	 * Given a list of possible Positions and a list of guesses already made, return
	 * the best next guess. "Best" is the guess that would reduce the number of
	 * possibilities the most.
	 */
	static Position bestTurn(List<Position> possible, List<Guess> guesses, List<Position> trialList) {

		// For each possible next move, see which reduces the possible list the
		// most on total.
		int minTotal = Integer.MAX_VALUE;
		int minPossibleTotal = Integer.MAX_VALUE;
		Position best = possible.get(0);
		
		List<Position> startingWords = new ArrayList<Position>();
		// Only include the Starting word analysis of there are no guesses.
		if (guesses.size() == 0) {
			for (String word : starting) {
				startingWords.add(new Position(word));
			}
			System.out.println("Starting Words: " + startingWords);
		}
		
		for (Position trial : trialList) {
			
//			if (countsNeeded) System.out.print(".");

			int total = 0;
			for (Position pos : possible) {
				if (!pos.equals(trial))
					total += possibleSize(possible, guesses, new Guess(pos, trial));
				
				if (!countsNeeded && total > minTotal)
					break;
			}
			
			for (Position word : startingWords)
				if (trial.equals(word))
					logger.info("*** for Starting Word guess " + trial + ", the size is " + total);
	
			if (total < minTotal) {
				logger.info("for best guess " + trial + ", the size is " + total);
				// Save the new best.
				minTotal = total;
				best = trial;
			}
			if (total < minPossibleTotal && possible.contains(trial)) {
				logger.info("for best possible guess " + trial + ", the size is " + total);
				// Save the new best.
				minPossibleTotal = total;
			}
		}
		return best;
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
					total += deepPossibleSize(possible, guesses, new Guess(pos, trial));
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
				total = deepPossibleSize(thisPossible, guesses, new Guess(pos, deepBestTurn(thisPossible, guesses, thisPossible)));
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
	static int possibleSize(List<Position> possible, List<Guess> guesses, Guess nextGuess) {
		guesses.add(nextGuess);
		int count = countPossible(possible, guesses);
		guesses.remove(nextGuess);
		return count;
	}

	
	/*
	 * Remove Positions from the list if they are impossible given the data in
	 * the guesses.
	 */
	static void removeImpossible(List<Position> possible, List<Guess> allGuesses) {
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
		int result = possible.size();
		for (Position p: possible) {
			for (Guess g : allGuesses) {
				Report hint = p.guess(g.getPos());
				if (!hint.equals(g.getScore())) {
					result--;
					break;
				}
			}
		}
		return result;
	}



}