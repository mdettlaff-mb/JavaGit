package mdettlaff.javagit.core;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;

public class GitObject {

	private final Type type;
	private final int size;
	private final ObjectContent content;

	public GitObject(Type type, int size, ObjectContent content) {
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

	public ObjectContent getContent() {
		return content;
	}

	public ObjectId computeId() {
		HashCode sha1 = Hashing.sha1().newHasher().putBytes(toByteArray()).hash();
		return new ObjectId(sha1.toString());
	}

	public byte[] toByteArray() {
		ByteArrayBuilder bytes = new ByteArrayBuilder();
		bytes.bytes(createHeader());
		bytes.bytes(content.toByteArray());
		return bytes.build();
	}

	private byte[] createHeader() {
		ByteArrayBuilder header = new ByteArrayBuilder();
		header.string(type.getLiteral());
		header.string(" ");
		header.string(String.valueOf(size));
		header.singleByte(0);
		return header.build();
	}

	@Override
	public String toString() {
		return content.toString();
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
