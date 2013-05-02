package mdettlaff.javagit.domain;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public class GitObject {

	private final Type type;
	private final int size;
	private final byte[] content;

	public GitObject(Type type, int size, byte[] content) {
		this.type = type;
		this.size = size;
		this.content = content;
	}

	public Type getType() {
		return type;
	}

	public int getSize() {
		return size;
	}

	public byte[] getContent() {
		return content;
	}

	public ObjectId computeId() {
		byte[] header = createHeader();
		byte[] hashBase = ArrayUtils.addAll(header, content);
		HashCode sha1 = Hashing.sha1().newHasher().putBytes(hashBase).hash();
		return new ObjectId(sha1.toString());
	}

	private byte[] createHeader() {
		StringBuilder header = new StringBuilder();
		header.append(type.getLiteral());
		header.append(' ');
		header.append(size);
		header.append((char) 0);
		return header.toString().getBytes();
	}

	public static enum Type {
		BLOB("blob"),
		TREE("tree"),
		COMMIT("commit"),
		TAG("tag");

		private final String literal;

		private Type(String literal) {
			this.literal = literal;
		}

		public String getLiteral() {
			return literal;
		}

		public static Type getByLiteral(String literal) {
			for (Type type : values()) {
				if (type.literal.equals(literal)) {
					return type;
				}
			}
			throw new IllegalArgumentException("Unknown git object type literal: " + literal);
		}
	}
}
