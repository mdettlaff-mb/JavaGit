package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.core.Blob;
import mdettlaff.javagit.core.GitObject;

import org.apache.commons.io.IOUtils;

public class HashObject {

	public static void main(String[] args) throws IOException {
		HashObject command = new HashObject();
		command.execute();
	}

	public void execute() throws IOException {
		byte[] content = IOUtils.toByteArray(System.in);
		GitObject object = new GitObject(GitObject.Type.BLOB, content.length, new Blob(content));
		System.out.println(object.computeId());
	}
}
