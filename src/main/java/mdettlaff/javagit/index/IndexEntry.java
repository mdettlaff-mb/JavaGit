package mdettlaff.javagit.index;

import mdettlaff.javagit.common.ObjectId;

public class IndexEntry {

	private final ObjectId id;
	private final String path;

	public IndexEntry(ObjectId id, String path) {
		this.id = id;
		this.path = path;
	}

	public ObjectId getId() {
		return id;
	}

	public String getPath() {
		return path;
	}

	@Override
	public String toString() {
		return path;
	}
}
