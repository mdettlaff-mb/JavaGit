package mdettlaff.javagit.core;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import mdettlaff.javagit.core.Tree.Node;
import mdettlaff.javagit.core.Tree.Node.Mode;

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
	public void testReadTree() throws Exception {
		InputStream rawTree = getClass().getResourceAsStream("tree");
		when(filesystem.openInput(".git/objects/be/42fc666262908364880b2c108ec02597d8b54a")).thenReturn(rawTree);
		// exercise
		GitObject result = objects.read(new ObjectId("be42fc666262908364880b2c108ec02597d8b54a"));
		// verify
		assertEquals(GitObject.Type.TREE, result.getType());
		assertEquals(103, result.getSize());
		assertTrue("content is not a tree", result.getContent() instanceof Tree);
		Tree tree = (Tree) result.getContent();
		List<Tree.Node> nodes = tree.getNodes();
		assertEquals(3, nodes.size());
		assertEquals(Tree.Node.Mode.NORMAL, nodes.get(0).getMode());
		StringBuilder builder = new StringBuilder();
		builder.append("100644 6433b6766d8372901881148308f0d000c8c416f8 .gitignore\n");
		builder.append("100755 f3f38869887ba7ba6ce945a35873f638e5c48f8b pom.xml\n");
		builder.append("040000 4eb25976ed157dd9fba6532b60bfb10cc02dce28 src\n");
		assertEquals(builder.toString(), result.toString());
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
	public void testWriteTree() throws Exception {
		ByteArrayOutputStream rawTree = new ByteArrayOutputStream();
		when(filesystem.openOutput(".git/objects/be/42fc666262908364880b2c108ec02597d8b54a")).thenReturn(rawTree);
		Node node1 = new Node(Mode.NORMAL, new ObjectId("6433b6766d8372901881148308f0d000c8c416f8"), ".gitignore");
		Node node2 = new Node(Mode.EXECUTABLE, new ObjectId("f3f38869887ba7ba6ce945a35873f638e5c48f8b"), "pom.xml");
		Node node3 = new Node(Mode.DIRECTORY, new ObjectId("4eb25976ed157dd9fba6532b60bfb10cc02dce28"), "src");
		ImmutableList<Node> nodes = ImmutableList.of(node1, node2, node3);
		ObjectContent tree = new Tree(nodes);
		GitObject object = new GitObject(GitObject.Type.TREE, 103, tree);
		// exercise
		objects.write(object);
		// verify
		byte[] expectedTree = IOUtils.toByteArray(getClass().getResourceAsStream("tree"));
		assertArrayEquals(expectedTree, rawTree.toByteArray());
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
