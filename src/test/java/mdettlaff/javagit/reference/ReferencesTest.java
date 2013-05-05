package mdettlaff.javagit.reference;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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

	@Test
	public void testResolveSymbolicRecursive() throws Exception {
		when(files.exists(Paths.get(".git", "foo"))).thenReturn(true);
		when(files.readAllChars(Paths.get(".git", "foo"))).thenReturn("ref: HEAD\n");
		ObjectId result = references.resolve("foo");
		assertEquals(new ObjectId("a1d0779b1946a31f5aa5b0da2f04260c9dc8c4f8"), result);
	}

	@Test
	public void testUpdate() throws Exception {
		references.update("refs/heads/master", new ObjectId("7b6a43154986e4d73db9e412f890900c185ac2b9"));
		verify(files).write(Paths.get(".git", "refs", "heads", "master"), "7b6a43154986e4d73db9e412f890900c185ac2b9\n");
	}

	@Test
	public void testUpdateSymbolic() throws Exception {
		references.update("HEAD", new ObjectId("7b6a43154986e4d73db9e412f890900c185ac2b9"));
		verify(files).write(Paths.get(".git", "refs", "heads", "master"), "7b6a43154986e4d73db9e412f890900c185ac2b9\n");
		verify(files, never()).write(Paths.get(".git", "HEAD"), "7b6a43154986e4d73db9e412f890900c185ac2b9\n");
	}

	@Test
	public void testUpdateSymbolicRecursive() throws Exception {
		when(files.exists(Paths.get(".git", "foo"))).thenReturn(true);
		when(files.readAllChars(Paths.get(".git", "foo"))).thenReturn("ref: HEAD\n");
		references.update("foo", new ObjectId("7b6a43154986e4d73db9e412f890900c185ac2b9"));
		verify(files).write(Paths.get(".git", "refs", "heads", "master"), "7b6a43154986e4d73db9e412f890900c185ac2b9\n");
		verify(files, never()).write(Paths.get(".git", "HEAD"), "7b6a43154986e4d73db9e412f890900c185ac2b9\n");
		verify(files, never()).write(Paths.get(".git", "foo"), "7b6a43154986e4d73db9e412f890900c185ac2b9\n");
	}
}
