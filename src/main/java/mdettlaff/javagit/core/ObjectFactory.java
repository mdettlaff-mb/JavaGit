package mdettlaff.javagit.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mdettlaff.javagit.core.GitObject.Type;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class ObjectFactory {

	public GitObject create(byte[] rawObject) {
		int firstSpaceIndex = ArrayUtils.indexOf(rawObject, (byte) ' ');
		int firstNullByteIndex = ArrayUtils.indexOf(rawObject, (byte) 0);
		String typeLiteral = new String(rawObject, 0, firstSpaceIndex);
		int sizeLength = firstNullByteIndex - (firstSpaceIndex + 1);
		int size = Integer.valueOf(new String(rawObject, firstSpaceIndex + 1, sizeLength));
		byte[] content = Arrays.copyOfRange(rawObject, firstNullByteIndex + 1, rawObject.length);
		verifySize(content, size);
		Type type = GitObject.Type.getByLiteral(typeLiteral);
		ObjectContent objectContent = createContent(type, content);
		return new GitObject(type, size, objectContent);
	}

	private ObjectContent createContent(Type type, byte[] content) {
		switch (type) {
		case BLOB:
			return createBlob(content);
		case COMMIT:
			return createCommit(content);
		default:
			throw new IllegalArgumentException("Unknown object type: " + type);
		}
	}

	private Blob createBlob(byte[] content) {
		return new Blob(content);
	}

	private ObjectContent createCommit(byte[] content) {
		CommitBuilder commit = new CommitBuilder();
		String[] lines = new String(content).split("\n");
		for (int i = 0; i < lines.length; i++) {
			String line = lines[i];
			if (line.startsWith("tree")) {
				commit.tree(new ObjectId(line.substring(5)));
			}
			if (line.startsWith("parent")) {
				commit.addParent(new ObjectId(line.substring(7)));
			}
			if (line.startsWith("author")) {
				commit.author(createCreator(line.substring(7)));
			}
			if (line.startsWith("committer")) {
				commit.committer(createCreator(line.substring(10)));
			}
			if (line.isEmpty()) {
				String[] messageLines = ArrayUtils.subarray(lines, i + 1, lines.length);
				commit.message(StringUtils.join(messageLines));
				break;
			}
		}
		return commit.build();
	}

	private Creator createCreator(String content) {
		Pattern pattern = Pattern.compile("(.*) <(.*)> ([0-9]+) ([+-]?[0-9]+)");
		Matcher matcher = pattern.matcher(content);
		Preconditions.checkArgument(matcher.matches(), "Invalid creator content: " + content);
		String name = matcher.group(1);
		String email = matcher.group(2);
		long instant = Long.valueOf(matcher.group(3)).longValue() * 1000;
		String timezone = matcher.group(4);
		DateTime date = new DateTime(instant).withZone(DateTimeZone.forID(timezone));
		return new Creator(name, email, date, timezone);
	}

	private void verifySize(byte[] content, int size) {
		if (content.length != size) {
			throw new IllegalArgumentException("Invalid content size: " + size);
		}
	}

	private static class CommitBuilder {

		private ObjectId tree;
		private List<ObjectId> parents;
		private Creator author;
		private Creator committer;
		private String message;

		public CommitBuilder() {
			parents = new ArrayList<>();
		}

		public void tree(ObjectId tree) {
			this.tree = tree;
		}

		public void addParent(ObjectId parent) {
			parents.add(parent);
		}

		public void author(Creator author) {
			this.author = author;
		}

		public void committer(Creator committer) {
			this.committer = committer;
		}

		public void message(String message) {
			this.message = message;
		}

		public Commit build() {
			return new Commit(tree, ImmutableList.copyOf(parents), author, committer, message);
		}
	}
}
