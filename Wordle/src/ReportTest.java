import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ReportTest {

	@Test
	public void testReportPositionPosition() {
		Report result = new Report(new Position("PERKY"), new Position("LARES"));
		Report expected = new Report(new ArrayList<String>(List.of("gray", "gray", "green", "yellow", "gray")));
		Assert.assertTrue(expected.equals(result));
		
		result = new Report(new Position("PERKY"), new Position("HERYE"));
		expected = new Report(new ArrayList<String>(List.of("gray", "green", "green", "yellow", "gray")));
		Assert.assertTrue(expected.equals(result));

		result = new Report(new Position("QUEUE"), new Position("EDUCE"));
		expected = new Report(new ArrayList<String>(List.of("yellow", "gray", "yellow", "gray", "green")));
		Assert.assertTrue(expected.equals(result));

		result = new Report(new Position("EMCEE"), new Position("PEEVE"));
		expected = new Report(new ArrayList<String>(List.of("gray", "yellow", "yellow", "gray", "green")));
		Assert.assertTrue(expected.equals(result));

		result = new Report(new Position("DINKY"), new Position("KINKY"));
		expected = new Report(new ArrayList<String>(List.of("gray", "green", "green", "green", "green")));
		Assert.assertTrue(expected.equals(result));

		result = new Report(new Position("ELDER"), new Position("OLDER"));
		expected = new Report(new ArrayList<String>(List.of("gray", "green", "green", "green", "green")));
		Assert.assertTrue(expected.equals(result));

	}

	
	
}
