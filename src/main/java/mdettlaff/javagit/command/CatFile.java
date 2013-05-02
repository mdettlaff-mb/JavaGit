package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObjects;
import mdettlaff.javagit.core.ObjectId;

public class CatFile implements Command {

	private final GitObjects objects;

	public CatFile(GitObjects objects) {
		this.objects = objects;
	}

	public void execute(String[] args) throws IOException {
		ObjectId id = new ObjectId(args[0]);
		GitObject object = objects.read(id);
		System.out.print(object);
	}
}
