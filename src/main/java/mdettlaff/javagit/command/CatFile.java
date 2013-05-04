package mdettlaff.javagit.command;

import java.io.IOException;

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
	public void execute(String[] args) throws IOException {
		Preconditions.checkArgument(args.length > 0, "Object ID parameter is required");
		ObjectId id = new ObjectId(args[0]);
		GitObject object = objects.read(id);
		System.out.print(object);
	}
}
