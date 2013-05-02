package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.db.GitObjects;
import mdettlaff.javagit.domain.GitObject;
import mdettlaff.javagit.domain.ObjectId;

public class CatFile {

	private final GitObjects objects;

	public CatFile(GitObjects objects) {
		this.objects = objects;
	}

	public static void main(String[] args) throws IOException {
		CatFile command = new CatFile(new GitObjects());
		command.execute(args);
	}

	public void execute(String[] args) throws IOException {
		ObjectId id = new ObjectId(args[0]);
		GitObject object = objects.read(id);
		System.out.println("type: " + object.getType().getLiteral());
		System.out.println("size: " + object.getSize());
		System.out.println();
		System.out.print(new String(object.getContent()));
	}
}
