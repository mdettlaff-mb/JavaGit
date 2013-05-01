package mdettlaff.javagit.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.InflaterInputStream;

import mdettlaff.javagit.domain.GitObject;
import mdettlaff.javagit.domain.ObjectId;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

public class GitObjects {

	public GitObject read(ObjectId id) throws IOException {
		StringBuilder path = new StringBuilder();
		path.append(".git/objects/");
		path.append(id.getValue().substring(0, 2));
		path.append("/");
		path.append(id.getValue().substring(2));
		InputStream fileInput = new FileInputStream(path.toString());
		InflaterInputStream input = new InflaterInputStream(fileInput);
		byte[] rawGitObject = IOUtils.toByteArray(input);
		int firstSpaceIndex = ArrayUtils.indexOf(rawGitObject, (byte) ' ');
		int firstNullByteIndex = ArrayUtils.indexOf(rawGitObject, (byte) 0);
		String typeLiteral = new String(rawGitObject, 0, firstSpaceIndex);
		int sizeLength = firstNullByteIndex - (firstSpaceIndex + 1);
		int size = Integer.valueOf(new String(rawGitObject, firstSpaceIndex + 1, sizeLength));
		byte[] content = Arrays.copyOfRange(rawGitObject, firstNullByteIndex + 1, rawGitObject.length);
		return new GitObject(GitObject.Type.getByLiteral(typeLiteral), size, content);
	}
}
