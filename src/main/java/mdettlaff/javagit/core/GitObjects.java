package mdettlaff.javagit.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;


import org.apache.commons.io.IOUtils;

public class GitObjects {

	private static final String GIT_DIR = ".git";

	private final Filesystem filesystem;

	public GitObjects(Filesystem filesystem) {
		this.filesystem = filesystem;
	}

	public GitObject read(ObjectId id) throws IOException {
		String path = getPath(id);
		InputStream inputStream = filesystem.readFile(path.toString());
		InflaterInputStream inflaterInputStream = new InflaterInputStream(inputStream);
		byte[] rawObject = IOUtils.toByteArray(inflaterInputStream);
		GitObject object = new ObjectFactory().create(rawObject);
		verifyId(object, id);
		return object;
	}

	private String getPath(ObjectId id) {
		StringBuilder path = new StringBuilder();
		path.append(GIT_DIR);
		path.append("/");
		path.append("objects");
		path.append("/");
		path.append(id.getValue().substring(0, 2));
		path.append("/");
		path.append(id.getValue().substring(2));
		return path.toString();
	}

	private void verifyId(GitObject object, ObjectId id) {
		ObjectId computedId = object.computeId();
		if (!computedId.equals(id)) {
			throw new IllegalArgumentException("Given object ID is invalid (computed: " + computedId + ", given: " + id + ")");
		}
	}
}
