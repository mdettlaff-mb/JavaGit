package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.object.Blob;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObjects;
import mdettlaff.javagit.object.GitObject.Type;

import org.apache.commons.io.IOUtils;

public class HashObject implements Command {

	private final GitObjects objects;

	public HashObject(GitObjects objects) {
		this.objects = objects;
	}

	@Override
	public void execute(String[] args) throws IOException {
		boolean write = args.length > 0 && args[0].equals("-w");
		byte[] content = IOUtils.toByteArray(System.in);
		GitObject object = new GitObject(Type.BLOB, content.length, new Blob(content));
		if (write) {
			System.out.println(objects.write(object));
		} else {
			System.out.println(object.computeId());
		}
	}
}
