
public class Guess {
	private Position p;
	private Report score;

	public Guess(Position p, Report s) {
		this.p = p;
		this.score = s;
	}
	
	public Guess(Position target, Position guess) {
		this.p = guess;
		this.score = target.guess(guess);
	}

	public Position getPos() {
		return p;
	}

	public Report getScore() {
		return score;
	}
	
	public String toString() {
		return p.toString()+" "+score.toString();
	}

	public boolean isSolved() {
		return score.isSolved();
	}

}
