package mdettlaff.javagit.command.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;

public class ArgumentsTest {

	@Test
	public void testNoParametersNoOptions() {
		// exercise
		Arguments args = new Arguments(new String[0]);
		// verify
		assertEquals(0, args.getParameters().size());
		assertFalse(args.isOptionSet("a"));
	}

	@Test
	public void testParameters() {
		assertEquals(Arrays.asList("foo"), new Arguments(new String[] {"foo"}).getParameters());
		assertEquals(Arrays.asList("x", "yz"), new Arguments(new String[] {"x", "yz"}).getParameters());
	}

	@Test
	public void testOptions() {
		assertTrue(new Arguments(new String[] {"-a"}).isOptionSet("a"));
		assertTrue(new Arguments(new String[] {"--foo"}).isOptionSet("foo"));
		assertTrue(new Arguments(new String[] {"-a", "-b"}).isOptionSet("b"));
	}

	@Test
	public void testParametersAndOptions() {
		// exercise
		Arguments args = new Arguments(new String[] {"-X", "foo"});
		// verify
		assertEquals(Arrays.asList("foo"), args.getParameters());
		assertTrue(args.isOptionSet("X"));
	}
}
