package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.core.Blob;
import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObject.Type;
import mdettlaff.javagit.core.GitObjects;

import org.apache.commons.io.IOUtils;

public class HashObject implements Command {

	private final GitObjects objects;

	public HashObject(GitObjects objects) {
		this.objects = objects;
	}

	@Override
	public void execute(String[] args) throws IOException {
		boolean write = args.length > 0 && args[0].equals("-w");
		hashObject(write);
	}

	private void hashObject(boolean write) throws IOException {
		byte[] content = IOUtils.toByteArray(System.in);
		GitObject object = new GitObject(Type.BLOB, content.length, new Blob(content));
		if (write) {
			System.out.println(objects.write(object));
		} else {
			System.out.println(object.computeId());
		}
	}
}
