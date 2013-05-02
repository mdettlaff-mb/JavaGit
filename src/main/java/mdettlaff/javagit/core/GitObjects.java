package mdettlaff.javagit.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
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
		InputStream input = filesystem.openInput(path.toString());
		try {
			input = new InflaterInputStream(input);
			byte[] rawObject = IOUtils.toByteArray(input);
			GitObject object = new ObjectFactory().create(rawObject);
			verifyId(object, id);
			return object;
		} finally {
			IOUtils.closeQuietly(input);
		}
	}

	public void write(GitObject object) throws IOException {
		ObjectId id = object.computeId();
		String path = getPath(id);
		if (!filesystem.exists(path)) {
			OutputStream output = filesystem.openOutput(path.toString());
			try {
				output = new DeflaterOutputStream(output);
				IOUtils.write(object.toByteArray(), output);
			} finally {
				IOUtils.closeQuietly(output);
			}
		}
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
			throw new IllegalArgumentException("Invalid object ID: " + id);
		}
	}
}
