package mdettlaff.javagit.domain;

import java.util.List;

public class Commit implements ObjectContent {

	private final ObjectId tree;
	private final List<ObjectId> parents;
	private final String author;
	private final String committer;
	private final String message;

	public Commit(ObjectId tree, List<ObjectId> parents, String author, String committer, String message) {
		this.tree = tree;
		this.parents = parents;
		this.author = author;
		this.committer = committer;
		this.message = message;
	}

	@Override
	public byte[] toByteArray() {
		// TODO mdettlaff implement
		return message.getBytes();
	}

	@Override
	public String toString() {
		return new String(message);
	}
}
