package mdettlaff.javagit.object;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import mdettlaff.javagit.common.Constants;
import mdettlaff.javagit.common.FilesWrapper;
import mdettlaff.javagit.common.ObjectId;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Preconditions;

public class GitObjects {

	private final FilesWrapper files;

	public GitObjects(FilesWrapper files) {
		this.files = files;
	}

	public GitObject read(ObjectId id) throws IOException {
		Path path = getPath(id);
		Preconditions.checkArgument(files.exists(path), "Unable to find object " + id);
		try (InputStream input = new InflaterInputStream(files.newInputStream(path))) {
			byte[] rawObject = IOUtils.toByteArray(input);
			GitObject object = new ObjectFactory().create(rawObject);
			verifyId(object, id);
			return object;
		}
	}

	public ObjectId write(GitObject object) throws IOException {
		ObjectId id = object.computeId();
		Path path = getPath(id);
		if (!files.exists(path)) {
			try (OutputStream output = new DeflaterOutputStream(files.newOutputStream(path))) {
				IOUtils.write(object.toByteArray(), output);
			}
		}
		return id;
	}

	private Path getPath(ObjectId id) {
		String prefix = id.getValue().substring(0, 2);
		String suffix = id.getValue().substring(2);
		return Paths.get(Constants.GIT_DIR, "objects", prefix, suffix);
	}

	private void verifyId(GitObject object, ObjectId id) {
		ObjectId computedId = object.computeId();
		Preconditions.checkState(computedId.equals(id), "Invalid object ID: " + id);
	}
}
