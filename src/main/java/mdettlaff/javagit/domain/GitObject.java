package mdettlaff.javagit.domain;

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
