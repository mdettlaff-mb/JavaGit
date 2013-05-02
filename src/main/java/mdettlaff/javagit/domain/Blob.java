package mdettlaff.javagit.domain;

public class Blob implements ObjectContent {

	private final byte[] content;

	public Blob(byte[] content) {
		this.content = content;
	}

	public byte[] getContent() {
		return content;
	}

	@Override
	public byte[] toByteArray() {
		return content;
	}
}
