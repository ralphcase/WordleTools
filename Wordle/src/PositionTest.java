import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class PositionTest {
	
    @Test
    public void testConstructorWithValidWord() {
        String word = "ABCDE";
        Position position = new Position(word);
        Assert.assertArrayEquals(word.toCharArray(), position.toCharArray());
    }

    @Test
    public void testConstructorWithInvalidWordLength() {
        String word = "ABCD";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Position(word));
    }

    @Test
    public void testConstructorWithInvalidCharacters() {
        String word = "12345";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Position(word));
    }
    
    @Test
    public void testGuess() {
        Position target = new Position("ABCDE");
        Position guess = new Position("ABCDF");
        Report report = target.guess(guess);
        String[] expectedHints = { "CORRECT", "CORRECT", "CORRECT", "CORRECT", "ABSENT"};
        Assert.assertTrue(report.equals(new Report(expectedHints)));
    }

    @Test
    public void testAllPositions() {
        List<Position> allWords = Position.getALLWORDS();
        Assert.assertFalse(allWords.isEmpty());
    }
    
    @Test
	public void TestPosition() {
		Position p1 = new Position("EUCRE");
		Report r1 = p1.guess(new Position("CROON"));
		Report expected = new Report(new ArrayList<String>(List.of("yellow", "yellow", "gray", "gray", "gray")));
		Assert.assertTrue(expected.equals(r1));
	}
	

}
