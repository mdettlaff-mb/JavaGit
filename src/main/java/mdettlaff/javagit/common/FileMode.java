package mdettlaff.javagit.common;

public enum FileMode {

	NORMAL("100644", 0xa4),
	EXECUTABLE("100755", 0xed),
	SYMLINK("120000", 0x00),
	DIRECTORY("40000", 0x00);

	private String literal;
	private byte signature;

	private FileMode(String literal, int signature) {
		this.literal = literal;
		this.signature = (byte) signature;
	}

	public String getLiteral() {
		return literal;
	}

	public byte getSignature() {
		return signature;
	}

	public static FileMode getByLiteral(String literal) {
		for (FileMode mode : values()) {
			if (mode.literal.equals(literal)) {
				return mode;
			}
		}
		throw new IllegalArgumentException("Unknown mode literal: " + literal);
	}

	public static FileMode getBySignature(byte signature) {
		for (FileMode mode : values()) {
			if (mode.signature == signature) {
				return mode;
			}
		}
		throw new IllegalArgumentException("Unknown mode signature: " + signature);
	}
}
