package mdettlaff.javagit.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.junit.Test;

public class GitObjectsTest {

	@Test
	public void testReadBlob() throws Exception {
		Filesystem filesystem = mock(Filesystem.class);
		InputStream blob = getClass().getResourceAsStream("blob");
		when(filesystem.openInput(".git/objects/3b/d1f0e29744a1f32b08d5650e62e2e62afb177c")).thenReturn(blob);
		GitObjects objects = new GitObjects(filesystem);
		// exercise
		GitObject result = objects.read(new ObjectId("3bd1f0e29744a1f32b08d5650e62e2e62afb177c"));
		// verify
		assertEquals(GitObject.Type.BLOB, result.getType());
		assertEquals(8, result.getSize());
		assertTrue(result.getContent() instanceof Blob);
		assertEquals("foo\nbar\n", new String(((Blob) result.getContent()).getContent()));
	}
}
