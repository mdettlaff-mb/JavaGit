package mdettlaff.javagit.command.plumbing;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.common.ObjectId;
import mdettlaff.javagit.config.Config;
import mdettlaff.javagit.object.Commit;
import mdettlaff.javagit.object.Creator;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObject.Type;
import mdettlaff.javagit.object.GitObjects;

import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public class CommitTree implements Command {

	private final GitObjects objects;
	private final Config config;

	public CommitTree(GitObjects objects, Config config) {
		this.objects = objects;
		this.config = config;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		List<String> parameters = args.getParameters();
		Preconditions.checkArgument(!parameters.isEmpty(), "Tree object ID parameter is required");
		String message = IOUtils.toString(System.in).trim();
		ObjectId tree = new ObjectId(parameters.get(0));
		List<ObjectId> parents = getParents(parameters);
		ObjectId commitObjectId = execute(tree, message, parents);
		System.out.println(commitObjectId);
	}

	private List<ObjectId> getParents(List<String> parameters) {
		List<ObjectId> parents = new ArrayList<>();
		for (String parentArgument : parameters.subList(1, parameters.size())) {
			parents.add(new ObjectId(parentArgument));
		}
		return parents;
	}

	public ObjectId execute(ObjectId tree, String message, List<ObjectId> parents) throws IOException {
		String name = config.get("user.name");
		String email = config.get("user.email");
		Creator author = new Creator(name, email, DateTime.now(), "+0200");
		Commit commit = new Commit(tree, ImmutableList.copyOf(parents), author, author, message);
		GitObject commitObject = new GitObject(Type.COMMIT, commit.toByteArray().length, commit);
		ObjectId id = objects.write(commitObject);
		return id;
	}
}
