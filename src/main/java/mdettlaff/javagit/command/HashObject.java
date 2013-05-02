package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.core.Blob;
import mdettlaff.javagit.core.Filesystem;
import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObjects;

import org.apache.commons.io.IOUtils;

public class HashObject {

	private final GitObjects objects;

	public HashObject(GitObjects objects) {
		this.objects = objects;
	}

	public static void main(String[] args) throws IOException {
		HashObject command = new HashObject(new GitObjects(new Filesystem()));
		boolean write = args.length > 0 && args[0].equals("-w");
		command.execute(write);
	}

	public void execute(boolean write) throws IOException {
		byte[] content = IOUtils.toByteArray(System.in);
		GitObject object = new GitObject(GitObject.Type.BLOB, content.length, new Blob(content));
		if (write) {
			objects.write(object);
		}
		System.out.println(object.computeId());
	}
}
