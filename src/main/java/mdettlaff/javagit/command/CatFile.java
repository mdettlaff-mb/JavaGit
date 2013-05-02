package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.core.Filesystem;
import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObjects;
import mdettlaff.javagit.core.ObjectId;

public class CatFile {

	private final GitObjects objects;

	public CatFile(GitObjects objects) {
		this.objects = objects;
	}

	public static void main(String[] args) throws IOException {
		CatFile command = new CatFile(new GitObjects(new Filesystem()));
		ObjectId id = new ObjectId(args[0]);
		command.execute(id);
	}

	public void execute(ObjectId id) throws IOException {
		GitObject object = objects.read(id);
		System.out.print(object);
	}
}
