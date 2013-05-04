package mdettlaff.javagit.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import mdettlaff.javagit.object.Commit;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObjects;
import mdettlaff.javagit.object.ObjectId;
import mdettlaff.javagit.object.GitObject.Type;

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
	public void execute(String[] args) throws IOException {
		Preconditions.checkArgument(args.length > 0, "Object ID parameter is required");
		boolean showMerges = !(args.length > 1 && args[1].equals("--no-merges"));
		List<ObjectId> ids = execute(new ObjectId(args[0]), showMerges);
		for (ObjectId id : ids) {
			System.out.println(id);
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
