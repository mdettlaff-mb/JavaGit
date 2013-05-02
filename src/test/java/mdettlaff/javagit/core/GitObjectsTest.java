package mdettlaff.javagit.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
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

	@Test
	public void testWriteBlob() throws Exception {
		Filesystem filesystem = mock(Filesystem.class);
		ByteArrayOutputStream blob = new ByteArrayOutputStream();
		when(filesystem.openOutput(".git/objects/3b/d1f0e29744a1f32b08d5650e62e2e62afb177c")).thenReturn(blob);
		GitObjects objects = new GitObjects(filesystem);
		GitObject object = new GitObject(GitObject.Type.BLOB, 8, new Blob("foo\nbar\n".getBytes()));
		// exercise
		objects.write(object);
		// verify
		byte[] expectedBlob = IOUtils.toByteArray(getClass().getResourceAsStream("blob"));
		assertArrayEquals(expectedBlob, blob.toByteArray());
	}
}
