package mdettlaff.javagit.core;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class ConfigTest {

	private Filesystem filesystem;
	private Config config;

	@Before
	public void setUp() {
		filesystem = mock(Filesystem.class);
		config = new Config(filesystem, "~/.gitconfig");
	}

	@Test
	public void test() throws Exception {
		prepareStream();
		assertEquals("Michał Dettlaff", config.get("user.name"));
		prepareStream();
		assertEquals("mdettlaff@jitsolutions.pl", config.get("user.email"));
		prepareStream();
		assertEquals("true", config.get("color.ui"));
		prepareStream();
		assertEquals("diff3", config.get("merge.conflictstyle"));
	}

	@Test(expected = IllegalStateException.class)
	public void testThrowingExceptionWhenConfigFileNotFound() throws Exception {
		when(filesystem.exists("~/.gitconfig")).thenReturn(false);
		config.get("user.name");
	}

	private void prepareStream() throws IOException {
		InputStream configContent = getClass().getResourceAsStream("gitconfig");
		when(filesystem.exists("~/.gitconfig")).thenReturn(true);
		when(filesystem.openInput("~/.gitconfig")).thenReturn(configContent);
	}
}
