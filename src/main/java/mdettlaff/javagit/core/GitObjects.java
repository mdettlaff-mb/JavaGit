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
		InputStream input = filesystem.read(path.toString());
		InflaterInputStream inflaterInput = new InflaterInputStream(input);
		byte[] rawObject = IOUtils.toByteArray(inflaterInput);
		GitObject object = new ObjectFactory().create(rawObject);
		verifyId(object, id);
		return object;
	}

	public void write(GitObject object) throws IOException {
		ObjectId id = object.computeId();
		String path = getPath(id);
		System.out.println("output: " + new String(object.toByteArray()));
		if (!filesystem.exists(path)) {
			OutputStream output = filesystem.write(path.toString());
			DeflaterOutputStream deflaterOutput = new DeflaterOutputStream(output);
			IOUtils.write(object.toByteArray(), deflaterOutput);
			deflaterOutput.close();
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
			throw new IllegalArgumentException("Given object ID is invalid (computed: " + computedId + ", given: " + id + ")");
		}
	}
}
