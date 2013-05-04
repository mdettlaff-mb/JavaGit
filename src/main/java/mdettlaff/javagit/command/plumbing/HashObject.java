package mdettlaff.javagit.command.plumbing;

import java.io.IOException;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.object.Blob;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObject.Type;
import mdettlaff.javagit.object.GitObjects;

import org.apache.commons.io.IOUtils;

public class HashObject implements Command {

	private final GitObjects objects;

	public HashObject(GitObjects objects) {
		this.objects = objects;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		boolean write = args.isOptionSet("w");
		byte[] content = IOUtils.toByteArray(System.in);
		GitObject object = new GitObject(Type.BLOB, content.length, new Blob(content));
		if (write) {
			System.out.println(objects.write(object));
		} else {
			System.out.println(object.computeId());
		}
	}
}
