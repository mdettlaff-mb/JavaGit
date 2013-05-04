package mdettlaff.javagit.command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import mdettlaff.javagit.core.Commit;
import mdettlaff.javagit.core.Config;
import mdettlaff.javagit.core.Creator;
import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObject.Type;
import mdettlaff.javagit.core.GitObjects;
import mdettlaff.javagit.core.ObjectId;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
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
	public void execute(String[] args) throws IOException {
		Preconditions.checkArgument(args.length > 0, "Tree object ID parameter is required");
		String message = IOUtils.toString(System.in).trim();
		List<ObjectId> parents = new ArrayList<>();
		for (String parentArgument : ArrayUtils.remove(args, 0)) {
			parents.add(new ObjectId(parentArgument));
		}
		commitTree(new ObjectId(args[0]), message, parents);
	}

	private void commitTree(ObjectId tree, String message, List<ObjectId> parents) throws IOException {
		String name = config.get("user.name");
		String email = config.get("user.email");
		Creator author = new Creator(name, email, DateTime.now(), "+0200");
		Commit commit = new Commit(tree, ImmutableList.copyOf(parents), author, author, message);
		GitObject commitObject = new GitObject(Type.COMMIT, commit.toByteArray().length, commit);
		ObjectId id = objects.write(commitObject);
		System.out.println(id);
	}
}