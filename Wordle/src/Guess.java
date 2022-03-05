
public class Guess {
	private Position p;	// This guess
	private Report score; // How well it matches the target.

	public Guess(Position p, Report s) {
		this.p = p;
		this.score = s;
	}
	
	public Guess(Position p, Position g) {
		this.p = p;
		this.score = g.guess(p);
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
