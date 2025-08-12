import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.List;
import java.util.logging.Logger;

public class BestStarter {

	private static Logger logger = Logger.getLogger(Solver.class.getName());
	private static final int TOPSIZE = 50;
	
	private static class Score implements Comparable<Score>{
		Position pos;
		int score;
		public Score(Position p, int s) {
			pos = p;
			score = s;
		}
		public String toString() {
			return "["+pos+":"+score+"]";
		}
		public int compareTo(Score o) {
			return Integer.compare(o.score, this.score);
		}
	}
	
	private static PriorityQueue<Score> top = new PriorityQueue<>();

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
	
		Position.writeWords(analyze(), "BestStarter.txt");

		long endTime = System.currentTimeMillis();
		System.out.println("It took " + (endTime - startTime) / 1000.0 + " seconds.");
	}
	

	private static String analyze() {
		List<Position> allWords = Position.getALLWORDS();
		List<Position> possible = Position.getGOALWORDS();
		List<Position> solutions = Position.getSOLUTIONWORDS();
		List<Guess> guesses = new ArrayList<Guess>();
		
		// Assume that previous solutions are not possible solutions.
		possible.removeAll(solutions);
				
		boolean hardMode = 
//				true;
				false;
						
		Solver.removeImpossible(possible, guesses);
		StringBuilder output = new StringBuilder();
		
		output.append("[" + guesses.size() + "] " + possible.size() + " possible: \t" + possible + "\n");
		output.append("Guesses: " + guesses + "\n");
		List<Score> best;
		if (hardMode)
			best = bestTurn(possible, guesses, possible);
		else
			best = bestTurn(possible, guesses, allWords);
		output.append("\nbest: " + best + "\n");	
		
		for (Score sc : best) {
			if (possible.contains(sc.pos)) {
				output.append("\t" + sc + "\n");
			}
		}
		return output.toString();
	}
	
	/*
	 * Given a list of possible Positions and a list of guesses already made, return
	 * the best next guess. "Best" is the guess that would reduce the number of
	 * possibilities the most.
	 */
	private static List<BestStarter.Score> bestTurn(List<Position> possible, List<Guess> guesses, List<Position> trialList) {

		// For each possible next move, see which reduces the possible list the
		// most on total.
		
		for (Position trial : trialList) {
			
			int total = 0;
			for (Position pos : possible) {
				if (!pos.equals(trial)) {
					total += Solver.possibleSize(possible, guesses, new Guess(pos, trial));
					if (top.size() == TOPSIZE && total > top.peek().score) {
						break;
					}
				}
			}
			
			// Only save a few, limited by TOPSIZE.
			top.offer(new Score(trial, total));
			if (top.size() > TOPSIZE) {
				top.poll();
			}
		}
		
		List<Score> topl = new ArrayList<Score>();
		while (top.size() > 0) {
			topl.add(0, top.poll());
		}
		
		return topl;
	}	

}
