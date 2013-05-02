package mdettlaff.javagit.command;

import java.io.IOException;

import com.google.common.base.Preconditions;

import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObjects;
import mdettlaff.javagit.core.ObjectId;

public class CatFile implements Command {

	private final GitObjects objects;

	public CatFile(GitObjects objects) {
		this.objects = objects;
	}

	public void execute(String[] args) throws IOException {
		Preconditions.checkArgument(args.length > 0, "Object ID parameter is required");
		ObjectId id = new ObjectId(args[0]);
		GitObject object = objects.read(id);
		System.out.print(object);
	}
}
