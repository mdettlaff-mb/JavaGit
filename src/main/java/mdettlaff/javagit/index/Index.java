package mdettlaff.javagit.index;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class Index {

	private final List<IndexEntry> entries;

	public Index(ImmutableList<IndexEntry> entries) {
		this.entries = entries;
	}

	public List<IndexEntry> getEntries() {
		return entries;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (IndexEntry entry : entries) {
			builder.append(entry).append('\n');
		}
		return builder.toString();
	}
}
