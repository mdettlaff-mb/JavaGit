package mdettlaff.javagit.object;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import mdettlaff.javagit.common.Constants;
import mdettlaff.javagit.common.FilenamePrefixFilter;
import mdettlaff.javagit.common.FilesWrapper;
import mdettlaff.javagit.common.ObjectId;

import org.apache.commons.io.IOUtils;

import com.google.common.base.Preconditions;

public class GitObjects {

	private static final String OBJECTS_PATH = Paths.get(Constants.GIT_DIR, "objects").toString();

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

	public ObjectId findUniqueId(String revision) throws IOException {
		Preconditions.checkArgument(revision.length() >= 4, "Revision too short: " + revision);
		String revisionPrefix = revision.substring(0, 2);
		String revisionSuffix = revision.substring(2);
		Path dir = Paths.get(OBJECTS_PATH, revisionPrefix);
		Preconditions.checkArgument(files.exists(dir), "Revision not found: " + revision);
		FilenamePrefixFilter filter = new FilenamePrefixFilter(revisionSuffix);
		List<ObjectId> ids = new ArrayList<>();
		try (DirectoryStream<Path> dirStream = files.newDirectoryStream(dir, filter)) {
			for (Path file : dirStream) {
				ObjectId id = new ObjectId(revisionPrefix + file.getFileName());
				ids.add(id);
			}
		}
		Preconditions.checkArgument(!ids.isEmpty(), "Revision not found: " + revision);
		Preconditions.checkArgument(ids.size() == 1, "Revision is not unique: " + revision);
		return ids.get(0);
	}

	private Path getPath(ObjectId id) {
		String prefix = id.getValue().substring(0, 2);
		String suffix = id.getValue().substring(2);
		return Paths.get(OBJECTS_PATH, prefix, suffix);
	}

	private void verifyId(GitObject object, ObjectId id) {
		ObjectId computedId = object.computeId();
		Preconditions.checkState(computedId.equals(id), "Invalid object ID: " + id);
	}
}
