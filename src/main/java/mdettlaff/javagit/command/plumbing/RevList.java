package mdettlaff.javagit.command.plumbing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.common.ObjectId;
import mdettlaff.javagit.object.Commit;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObject.Type;
import mdettlaff.javagit.object.GitObjects;

import com.google.common.base.Preconditions;

public class RevList implements Command {

	private final GitObjects objects;

	private Queue<ObjectId> queue;
	private Set<ObjectId> open;
	private Set<ObjectId> closed;

	public RevList(GitObjects objects) {
		this.objects = objects;
		this.queue = new LinkedList<>();
		this.open = new HashSet<>();
		this.closed = new HashSet<>();
	}

	@Override
	public void execute(Arguments args) throws IOException {
		Preconditions.checkArgument(!args.getParameters().isEmpty(), "Object ID parameter is required");
		ObjectId id = new ObjectId(args.getParameters().get(0));
		boolean showMerges = args.isOptionSet("no-merges");
		List<ObjectId> ids = execute(id, showMerges);
		for (ObjectId currentId : ids) {
			System.out.println(currentId);
		}
	}

	public List<ObjectId> execute(ObjectId id, boolean showMerges) throws IOException {
		// use breadth-first search to traverse history
		List<ObjectId> ids = new ArrayList<>();
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
				ids.add(currentId);
			}
			closed.add(currentId);
		}
		return ids;
	}

	private Commit readCommit(ObjectId id) throws IOException {
		GitObject object = objects.read(id);
		Preconditions.checkArgument(object.getType() == Type.COMMIT, "Object with given ID must be a commit");
		return (Commit) object.getContent();
	}
}
