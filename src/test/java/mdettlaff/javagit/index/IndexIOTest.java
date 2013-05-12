package mdettlaff.javagit.index;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Paths;
import java.util.List;

import mdettlaff.javagit.common.FilesWrapper;
import mdettlaff.javagit.common.ObjectId;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

public class IndexIOTest {

	private FilesWrapper files;
	private IndexIO indexIO;

	@Before
	public void setUp() throws Exception {
		files = mock(FilesWrapper.class);
		indexIO = new IndexIO(files);
		when(files.exists(Paths.get(".git", "index"))).thenReturn(true);
		byte[] rawIndex = IOUtils.toByteArray(getClass().getResource("index"));
		when(files.readAllBytes(Paths.get(".git", "index"))).thenReturn(rawIndex);
	}

	@Test
	public void testRead() throws Exception {
		Index result = indexIO.read();
		List<IndexEntry> entries = result.getEntries();
		assertEquals(38, entries.size());
		assertEquals(new ObjectId("6433b6766d8372901881148308f0d000c8c416f8"), entries.get(0).getId());
		assertEquals(".gitignore", entries.get(0).getPath());
		assertEquals(new ObjectId("0539a9efae831c8cd469dc8d4edda6ba57781013"), entries.get(2).getId());
		assertEquals("src/main/java/mdettlaff/javagit/command/Git.java", entries.get(2).getPath());
		String expectedFiles = IOUtils.toString(getClass().getResource("files"));
		assertEquals(expectedFiles, result.toString());
	}
}
