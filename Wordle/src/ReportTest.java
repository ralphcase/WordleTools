import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ReportTest {

	@Test
	public void testReportConstructor() {
		Position target = new Position("ABCDE");
		Position guess = new Position("ABDCF");
		String[] answer = {"CORRECT", "CORRECT", "PRESENT", "PRESENT", "ABSENT" };

		Report report = new Report(target, guess);
		assert(report.equals(new Report(answer)));
	}

	@Test
	public void testReportStringArrayConstructor() {
		String[] input = { "GREEN", "GRAY", "RED", "YELLOW", "GRAY" };
		String[] input2 = {"CORRECT", "ABSENT", "CORRECT", "PRESENT", "ABSENT" };

		Report report = new Report(input);
		assert(report.equals(new Report(input2)));
	}

	@Test
	public void testReportListConstructor() {
		List<String> input1 = Arrays.asList("RED", "GREEN", "ABSENT", "YELLOW", "GREEN");
		List<String> input2 = Arrays.asList("CORRECT", "CORRECT", "ABSENT", "PRESENT", "CORRECT");

		Report report = new Report(input1);
		assert(report.equals(new Report(input2)));
	}

	@Test
	public void testToString() {
		String[] input = { "RED", "ABSENT", "PRESENT", "GREEN", "GREEN" };

		Report report = new Report(input);
		assertEquals("[" 
					+ Report.ANSI_GREEN + "CORRECT" + Report.ANSI_RESET + ", " 
					+ Report.ANSI_GRAY + "ABSENT" + Report.ANSI_RESET + ", " 
					+ Report.ANSI_YELLOW + "PRESENT" + Report.ANSI_RESET + ", "
					+ Report.ANSI_GREEN + "CORRECT" + Report.ANSI_RESET + ", " 
					+ Report.ANSI_GREEN + "CORRECT" + Report.ANSI_RESET + "]", 
				report.toString());
	}

	@Test
	public void testEquals() {
		String[] input1 = { "RED", "ABSENT", "PRESENT", "GREEN", "RED" };
		String[] input2 = { "RED", "ABSENT", "PRESENT", "GREEN", "GREEN" };

		Report report1 = new Report(input1);
		Report report2 = new Report(input2);
		assertTrue(report1.equals(report2));
	}

	@Test
	public void testIsSolved() {
		String[] input = { "GREEN", "GREEN", "GREEN", "GREEN", "GREEN" };

		Report report = new Report(input);
		assertTrue(report.isSolved());
	}

	@Test
	public void testReportPositionPosition() {
		Report result = new Report(new Position("PERKY"), new Position("LARES"));
		Report expected = new Report(new ArrayList<String>(List.of("gray", "gray", "green", "yellow", "gray")));
		assertTrue(expected.equals(result));

		result = new Report(new Position("PERKY"), new Position("HERYE"));
		expected = new Report(new ArrayList<String>(List.of("gray", "green", "green", "yellow", "gray")));
		assertTrue(expected.equals(result));

		result = new Report(new Position("QUEUE"), new Position("EDUCE"));
		expected = new Report(new ArrayList<String>(List.of("yellow", "gray", "yellow", "gray", "green")));
		assertTrue(expected.equals(result));

		result = new Report(new Position("EMCEE"), new Position("PEEVE"));
		expected = new Report(new ArrayList<String>(List.of("gray", "yellow", "yellow", "gray", "green")));
		assertTrue(expected.equals(result));

		result = new Report(new Position("DINKY"), new Position("KINKY"));
		expected = new Report(new ArrayList<String>(List.of("gray", "green", "green", "green", "green")));
		assertTrue(expected.equals(result));

		result = new Report(new Position("ELDER"), new Position("OLDER"));
		expected = new Report(new ArrayList<String>(List.of("gray", "green", "green", "green", "green")));
		assertTrue(expected.equals(result));

	}

}
