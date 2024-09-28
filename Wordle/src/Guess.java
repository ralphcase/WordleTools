
public class Guess {
	private Position position;
	private Report score;

	public Guess(Position p, Report s) {
		this.position = p;
		this.score = s;
	}

	public Guess(Position target, Position guess) {
		this(guess, target.guess(guess));
	}

	public Position getPos() {
		return position;
	}

	public Report getScore() {
		return score;
	}

	@Override
	public String toString() {
		return position.toString() + " " + score.toString();
	}

	public boolean isSolved() {
		return score.isSolved();
	}

}
