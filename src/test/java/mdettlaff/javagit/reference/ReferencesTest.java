package mdettlaff.javagit.reference;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;

import mdettlaff.javagit.common.FilesWrapper;
import mdettlaff.javagit.common.ObjectId;

import org.junit.Before;
import org.junit.Test;

public class ReferencesTest {

	private FilesWrapper files;
	private References references;

	@Before
	public void setUp() throws Exception {
		files = mock(FilesWrapper.class);
		references = new References(files);
		when(files.exists(Paths.get(".git", "refs", "heads", "master"))).thenReturn(true);
		when(files.readAllChars(Paths.get(".git", "refs", "heads", "master"))).thenReturn("a1d0779b1946a31f5aa5b0da2f04260c9dc8c4f8\n");
		when(files.exists(Paths.get(".git", "HEAD"))).thenReturn(true);
		when(files.readAllChars(Paths.get(".git", "HEAD"))).thenReturn("ref: refs/heads/master\n");
	}

	@Test
	public void testResolve() throws Exception {
		ObjectId result = references.resolve("refs/heads/master");
		assertEquals(new ObjectId("a1d0779b1946a31f5aa5b0da2f04260c9dc8c4f8"), result);
	}

	@Test
	public void testResolveSymbolic() throws Exception {
		ObjectId result = references.resolve("HEAD");
		assertEquals(new ObjectId("a1d0779b1946a31f5aa5b0da2f04260c9dc8c4f8"), result);
	}
}
