package mdettlaff.javagit.object;

import java.util.List;

import mdettlaff.javagit.common.ObjectId;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class Commit implements ObjectContent {

	private final ObjectId tree;
	private final List<ObjectId> parents;
	private final Creator author;
	private final Creator committer;
	private final String message;

	public Commit(ObjectId tree, ImmutableList<ObjectId> parents, Creator author, Creator committer, String message) {
		Preconditions.checkNotNull(parents, "List of commit parents cannot be null");
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

	public boolean isMerge() {
		return parents.size() > 1;
	}

	@Override
	public byte[] toByteArray() {
		ByteArrayBuilder bytes = new ByteArrayBuilder();
		bytes.field("tree", tree);
		for (ObjectId parent : parents) {
			bytes.field("parent", parent);
		}
		bytes.field("author", author.toByteArray());
		bytes.field("committer", committer.toByteArray());
		bytes.newline();
		bytes.line(message);
		return bytes.build();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("tree ").append(tree).append('\n');
		for (ObjectId parent : parents) {
			builder.append("parent ").append(parent).append('\n');
		}
		builder.append("author ").append(author).append('\n');
		builder.append("committer ").append(committer).append('\n');
		builder.append('\n');
		builder.append(message).append('\n');
		return builder.toString();
	}
}
