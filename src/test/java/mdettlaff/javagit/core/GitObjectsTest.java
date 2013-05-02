package mdettlaff.javagit.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class GitObjectsTest {

	private Filesystem filesystem;
	private GitObjects objects;

	@Before
	public void setUp() {
		filesystem = mock(Filesystem.class);
		objects = new GitObjects(filesystem);
	}

	@Test
	public void testReadBlob() throws Exception {
		InputStream rawBlob = getClass().getResourceAsStream("blob");
		when(filesystem.openInput(".git/objects/3b/d1f0e29744a1f32b08d5650e62e2e62afb177c")).thenReturn(rawBlob);
		// exercise
		GitObject result = objects.read(new ObjectId("3bd1f0e29744a1f32b08d5650e62e2e62afb177c"));
		// verify
		assertEquals(GitObject.Type.BLOB, result.getType());
		assertEquals(8, result.getSize());
		assertTrue("content is not a blob", result.getContent() instanceof Blob);
		Blob blob = (Blob) result.getContent();
		assertEquals("foo\nbar\n", new String(blob.getContent()));
		assertEquals("foo\nbar\n", result.toString());
	}

	@Test
	public void testReadCommit() throws Exception {
		InputStream rawCommit = getClass().getResourceAsStream("commit");
		when(filesystem.openInput(".git/objects/b9/2ec3607cf250278ad82231564fbb2b92e34a79")).thenReturn(rawCommit);
		// exercise
		GitObject result = objects.read(new ObjectId("b92ec3607cf250278ad82231564fbb2b92e34a79"));
		// verify
		assertEquals(GitObject.Type.COMMIT, result.getType());
		assertEquals(258, result.getSize());
		assertTrue("content is not a commit", result.getContent() instanceof Commit);
		Commit commit = (Commit) result.getContent();
		assertEquals(new ObjectId("5ddc4f4ddac21654260395b4767eaf43da4d0c63"), commit.getTree());
		List<ObjectId> parents = commit.getParents();
		assertEquals(1, parents.size());
		assertEquals(new ObjectId("02151e56a26e2735264b95236e4d5a24dad9a8ac"), parents.get(0));
		assertCreator(commit.getAuthor());
		assertCreator(commit.getCommitter());
		assertEquals("jar with dependencies", commit.getMessage());
		StringBuilder builder = new StringBuilder();
		builder.append("tree 5ddc4f4ddac21654260395b4767eaf43da4d0c63\n");
		builder.append("parent 02151e56a26e2735264b95236e4d5a24dad9a8ac\n");
		builder.append("author Michał Dettlaff <mdettlaff@jitsolutions.pl> 1367506510 +0200\n");
		builder.append("committer Michał Dettlaff <mdettlaff@jitsolutions.pl> 1367506510 +0200\n");
		builder.append("\n");
		builder.append("jar with dependencies\n");
		assertEquals(builder.toString(), result.toString());
	}

	private void assertCreator(Creator creator) {
		assertEquals("Michał Dettlaff", creator.getName());
		assertEquals("mdettlaff@jitsolutions.pl", creator.getEmail());
		DateTimeZone authorTimezone = creator.getDate().getZone();
		assertEquals("+02:00", authorTimezone.getID());
		assertEquals(new DateTime(2013, 5, 2, 16, 55, 10, 0, authorTimezone), creator.getDate());
	}

	@Test
	public void testWriteBlob() throws Exception {
		ByteArrayOutputStream rawBlob = new ByteArrayOutputStream();
		when(filesystem.openOutput(".git/objects/3b/d1f0e29744a1f32b08d5650e62e2e62afb177c")).thenReturn(rawBlob);
		GitObject object = new GitObject(GitObject.Type.BLOB, 8, new Blob("foo\nbar\n".getBytes()));
		// exercise
		objects.write(object);
		// verify
		byte[] expectedBlob = IOUtils.toByteArray(getClass().getResourceAsStream("blob"));
		assertArrayEquals(expectedBlob, rawBlob.toByteArray());
	}

	@Test
	public void testWriteCommit() throws Exception {
		ByteArrayOutputStream rawCommit = new ByteArrayOutputStream();
		when(filesystem.openOutput(".git/objects/b9/2ec3607cf250278ad82231564fbb2b92e34a79")).thenReturn(rawCommit);
		ObjectId tree = new ObjectId("5ddc4f4ddac21654260395b4767eaf43da4d0c63");
		ImmutableList<ObjectId> parents = ImmutableList.of(new ObjectId("02151e56a26e2735264b95236e4d5a24dad9a8ac"));
		Creator author = prepareCreator();
		Creator committer = prepareCreator();
		String message = "jar with dependencies";
		Commit commit = new Commit(tree, parents, author, committer, message);
		GitObject object = new GitObject(GitObject.Type.COMMIT, 258, commit);
		// exercise
		objects.write(object);
		// verify
		byte[] expectedCommit = IOUtils.toByteArray(getClass().getResourceAsStream("commit"));
		assertArrayEquals(expectedCommit, rawCommit.toByteArray());
	}

	private Creator prepareCreator() {
		String name = "Michał Dettlaff";
		String email = "mdettlaff@jitsolutions.pl";
		String timezone = "+0200";
		DateTime date = new DateTime(2013, 5, 2, 16, 55, 10, 0, DateTimeZone.forID(timezone));
		return new Creator(name, email, date, timezone);
	}
}
