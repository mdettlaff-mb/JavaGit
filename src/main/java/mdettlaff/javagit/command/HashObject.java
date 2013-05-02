package mdettlaff.javagit.command;

import java.io.IOException;

import mdettlaff.javagit.domain.GitObject;

import org.apache.commons.io.IOUtils;

public class HashObject {

	public static void main(String[] args) throws IOException {
		HashObject command = new HashObject();
		command.execute();
	}

	public void execute() throws IOException {
		byte[] content = IOUtils.toByteArray(System.in);
		GitObject object = new GitObject(GitObject.Type.BLOB, content.length, content);
		System.out.println(object.computeId());
	}
}
