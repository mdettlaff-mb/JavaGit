package mdettlaff.javagit.index;

import java.nio.file.Path;
import java.util.List;

import mdettlaff.javagit.common.FileMode;
import mdettlaff.javagit.common.ObjectId;

import com.google.common.collect.ImmutableList;

public class Index {

	private final List<Entry> entries;

	public Index(ImmutableList<Entry> entries) {
		this.entries = entries;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Entry entry : entries) {
			builder.append(entry).append('\n');
		}
		return builder.toString();
	}

	public static class Entry {

		private final Path path;
		private final ObjectId id;
		private final FileMode mode;

		public Entry(Path path, ObjectId id, FileMode mode) {
			this.path = path;
			this.id = id;
			this.mode = mode;
		}

		public ObjectId getId() {
			return id;
		}

		public Path getPath() {
			return path;
		}

		public FileMode getMode() {
			return mode;
		}

		@Override
		public String toString() {
			return mode.getLiteral() + ' ' + id + '\t' + path;
		}
	}
}
