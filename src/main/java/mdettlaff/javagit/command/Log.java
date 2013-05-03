package mdettlaff.javagit.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import mdettlaff.javagit.core.Commit;
import mdettlaff.javagit.core.Creator;
import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObject.Type;
import mdettlaff.javagit.core.GitObjects;
import mdettlaff.javagit.core.ObjectId;

import com.google.common.base.Preconditions;

public class Log implements Command {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private final GitObjects objects;

	private Queue<ObjectId> queue;
	private Set<ObjectId> open;
	private Set<ObjectId> closed;

	public Log(GitObjects objects) {
		this.objects = objects;
		this.queue = new LinkedList<>();
		this.open = new HashSet<>();
		this.closed = new HashSet<>();
	}

	@Override
	public void execute(String[] args) throws IOException {
		Preconditions.checkArgument(args.length > 0, "Object ID parameter is required");
		boolean showMerges = !(args.length > 1 && args[1].equals("--no-merges"));
		log(new ObjectId(args[0]), showMerges);
	}

	private void log(ObjectId id, boolean showMerges) throws IOException {
		// use breadth-first search to traverse history
		open.add(id);
		queue.add(id);
		while (!queue.isEmpty()) {
			ObjectId currentId = queue.remove();
			Commit commit = readCommit(currentId);
			for (ObjectId parentId : commit.getParents()) {
				if (!open.contains(parentId) && !closed.contains(parentId)) {
					open.add(parentId);
					queue.add(parentId);
				}
			}
			if (!commit.isMerge() || showMerges) {
				String commitAsString = commitToString(commit, currentId);
				System.out.println(commitAsString);
			}
			closed.add(currentId);
		}
	}

	private Commit readCommit(ObjectId id) throws IOException {
		GitObject object = objects.read(id);
		Preconditions.checkArgument(object.getType() == Type.COMMIT, "Object with given ID must be a commit");
		return (Commit) object.getContent();
	}

	private String commitToString(Commit commit, ObjectId id) {
		StringBuilder builder = new StringBuilder();
		builder.append("commit ").append(id).append('\n');
		Creator author = commit.getAuthor();
		builder.append("Author:\t").append(author.getName()).append(' ');
		builder.append('<').append(author.getEmail()).append('>');
		builder.append('\n');
		String date = new SimpleDateFormat(DATE_FORMAT).format(author.getDate().toDate());
		builder.append("Date:\t").append(date).append(' ');
		builder.append(author.getTimezone()).append('\n');
		builder.append('\n');
		builder.append(commit.getMessage()).append('\n');
		return builder.toString();
	}
}
