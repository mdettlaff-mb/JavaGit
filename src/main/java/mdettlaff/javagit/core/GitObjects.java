package mdettlaff.javagit.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Preconditions;

public class GitObjects {

	private static final String GIT_DIR = ".git";

	private final Filesystem filesystem;

	public GitObjects(Filesystem filesystem) {
		this.filesystem = filesystem;
	}

	public GitObject read(ObjectId id) throws IOException {
		String path = getPath(id);
		Preconditions.checkArgument(filesystem.exists(path), "Unable to find object " + id);
		try (InputStream input = new InflaterInputStream(filesystem.openInput(path))) {
			byte[] rawObject = IOUtils.toByteArray(input);
			GitObject object = new ObjectFactory().create(rawObject);
			verifyId(object, id);
			return object;
		}
	}

	public ObjectId write(GitObject object) throws IOException {
		ObjectId id = object.computeId();
		String path = getPath(id);
		if (!filesystem.exists(path)) {
			try (OutputStream output = new DeflaterOutputStream(filesystem.openOutput(path))) {
				IOUtils.write(object.toByteArray(), output);
			}
		}
		return id;
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
		Preconditions.checkState(computedId.equals(id), "Invalid object ID: " + id);
	}
}
