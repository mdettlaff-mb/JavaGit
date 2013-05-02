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
		ObjectId id = new ObjectId(args[0]);
		command.execute(id);
	}

	public void execute(ObjectId id) throws IOException {
		GitObject object = objects.read(id);
		System.out.println("type: " + object.getType().getLiteral());
		System.out.println("size: " + object.getSize());
		System.out.println();
		System.out.print(new String(object.getContent().toByteArray()));
	}
}
