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
		byte[] rawObject = IOUtils.toByteArray(input);
		int firstSpaceIndex = ArrayUtils.indexOf(rawObject, (byte) ' ');
		int firstNullByteIndex = ArrayUtils.indexOf(rawObject, (byte) 0);
		String typeLiteral = new String(rawObject, 0, firstSpaceIndex);
		int sizeLength = firstNullByteIndex - (firstSpaceIndex + 1);
		int size = Integer.valueOf(new String(rawObject, firstSpaceIndex + 1, sizeLength));
		byte[] content = Arrays.copyOfRange(rawObject, firstNullByteIndex + 1, rawObject.length);
		GitObject object = new GitObject(GitObject.Type.getByLiteral(typeLiteral), size, content);
		verifyId(object, id);
		return object;
	}

	private void verifyId(GitObject object, ObjectId id) {
		ObjectId computedId = object.computeId();
		if (!computedId.equals(id)) {
			throw new IllegalArgumentException("Given object ID is invalid (computed: " + computedId + ", given: " + id + ")");
		}
	}
}
