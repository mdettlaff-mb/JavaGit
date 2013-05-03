package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObjects;
import mdettlaff.javagit.core.ObjectId;

import com.google.common.base.Preconditions;

public class CatFile implements Command {

	private final GitObjects objects;

	public CatFile(GitObjects objects) {
		this.objects = objects;
	}

	@Override
	public void execute(String[] args) throws IOException {
		Preconditions.checkArgument(args.length > 0, "Object ID parameter is required");
		catFile(new ObjectId(args[0]));
	}

	private void catFile(ObjectId id) throws IOException {
		GitObject object = objects.read(id);
		System.out.print(object);
	}
}
