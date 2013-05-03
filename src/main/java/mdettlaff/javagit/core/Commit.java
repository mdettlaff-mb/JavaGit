package mdettlaff.javagit.core;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class Commit implements ObjectContent {

	private final ObjectId tree;
	private final List<ObjectId> parents;
	private final Creator author;
	private final Creator committer;
	private final String message;

	public Commit(ObjectId tree, ImmutableList<ObjectId> parents, Creator author, Creator committer, String message) {
		this.tree = tree;
		this.parents = parents;
		this.author = author;
		this.committer = committer;
		this.message = message;
	}

	public ObjectId getTree() {
		return tree;
	}

	public List<ObjectId> getParents() {
		return parents;
	}

	public Creator getAuthor() {
		return author;
	}

	public Creator getCommitter() {
		return committer;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public byte[] toByteArray() {
		StringBuilder content = new StringBuilder();
		content.append("tree " + tree + "\n");
		for (ObjectId parent : parents) {
			content.append("parent " + parent + "\n");
		}
		content.append("author ");
		content.append(new String(author.toByteArray()) + "\n");
		content.append("committer ");
		content.append(new String(committer.toByteArray()) + "\n");
		content.append('\n');
		content.append(message + "\n");
		return content.toString().getBytes();
	}

	@Override
	public String toString() {
		StringBuilder content = new StringBuilder();
		content.append("tree " + tree + "\n");
		for (ObjectId parent : parents) {
			content.append("parent " + parent + "\n");
		}
		content.append("author " + author + "\n");
		content.append("committer " + committer + "\n");
		content.append('\n');
		content.append(message + "\n");
		return content.toString();
	}
}
