package mdettlaff.javagit.core;

import java.util.Arrays;

import mdettlaff.javagit.core.GitObject.Type;

import org.apache.commons.lang3.ArrayUtils;

public class ObjectFactory {

	public GitObject create(byte[] rawObject) {
		int firstSpaceIndex = ArrayUtils.indexOf(rawObject, (byte) ' ');
		int firstNullByteIndex = ArrayUtils.indexOf(rawObject, (byte) 0);
		String typeLiteral = new String(rawObject, 0, firstSpaceIndex);
		int sizeLength = firstNullByteIndex - (firstSpaceIndex + 1);
		int size = Integer.valueOf(new String(rawObject, firstSpaceIndex + 1, sizeLength));
		byte[] content = Arrays.copyOfRange(rawObject, firstNullByteIndex + 1, rawObject.length);
		verifySize(content, size);
		Type type = GitObject.Type.getByLiteral(typeLiteral);
		ObjectContent objectContent = createContent(type, content);
		GitObject object = new GitObject(type, size, objectContent);
		return object;
	}

	private ObjectContent createContent(Type type, byte[] content) {
		switch (type) {
		case BLOB:
			return createBlob(content);
		case COMMIT:
			return createCommit(content);
		default:
			throw new IllegalArgumentException("Unknown object type: " + type);
		}
	}

	private Blob createBlob(byte[] content) {
		return new Blob(content);
	}

	private ObjectContent createCommit(byte[] content) {
		String message = new String(content);
		return new Commit(null, null, null, null, message);
	}

	private void verifySize(byte[] content, int size) {
		if (content.length != size) {
			throw new IllegalArgumentException("Invalid content size: " + size);
		}
	}
}
