import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class PositionTest {
	
	@Test
	public void TestPosition() {
		Position p1 = new Position("EUCRE");
		Report r1 = p1.guess(new Position("CROON"));
		Report expected = new Report(new ArrayList<String>(List.of("yellow", "yellow", "gray", "gray", "gray")));
		Assert.assertTrue(expected.equals(r1));
	}
	

}
