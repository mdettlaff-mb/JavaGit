package mdettlaff.javagit.index;

import java.nio.file.Path;

import mdettlaff.javagit.common.ObjectId;

public class IndexEntry {

	private final ObjectId id;
	private final Path path;

	public IndexEntry(ObjectId id, Path path) {
		this.id = id;
		this.path = path;
	}

	public ObjectId getId() {
		return id;
	}

	public Path getPath() {
		return path;
	}

	@Override
	public String toString() {
		return path.toString();
	}
}
