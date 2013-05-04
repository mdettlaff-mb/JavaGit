package mdettlaff.javagit.config;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import mdettlaff.javagit.common.FilesWrapper;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class ConfigTest {

	private static final Path CONFIG_PATH = Paths.get("~", ".gitconfig");

	private FilesWrapper files;
	private Config config;

	@Before
	public void setUp() {
		files = mock(FilesWrapper.class);
		config = new Config(files, CONFIG_PATH);
	}

	@Test
	public void test() throws Exception {
		String configContent = IOUtils.toString(getClass().getResource("gitconfig"));
		when(files.exists(CONFIG_PATH)).thenReturn(true);
		when(files.readAllLines(CONFIG_PATH)).thenReturn(Arrays.asList(configContent.split("\n")));
		assertEquals("Michał Dettlaff", config.get("user.name"));
		assertEquals("mdettlaff@jitsolutions.pl", config.get("user.email"));
		assertEquals("true", config.get("color.ui"));
		assertEquals("diff3", config.get("merge.conflictstyle"));
	}

	@Test(expected = IllegalStateException.class)
	public void testThrowingExceptionWhenConfigFileNotFound() throws Exception {
		when(files.exists(CONFIG_PATH)).thenReturn(false);
		config.get("user.name");
	}
}
