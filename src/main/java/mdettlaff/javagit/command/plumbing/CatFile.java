package mdettlaff.javagit.command.plumbing;

import java.io.IOException;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObjects;
import mdettlaff.javagit.object.ObjectId;

import com.google.common.base.Preconditions;

public class CatFile implements Command {

	private final GitObjects objects;

	public CatFile(GitObjects objects) {
		this.objects = objects;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		Preconditions.checkArgument(!args.getParameters().isEmpty(), "Object ID parameter is required");
		ObjectId id = new ObjectId(args.getParameters().get(0));
		boolean prettyPrint = args.isOptionSet("p");
		GitObject object = objects.read(id);
		if (prettyPrint) {
			System.out.print(object);
		} else {
			System.out.print(new String(object.getContent().toByteArray()));
		}
	}
}
