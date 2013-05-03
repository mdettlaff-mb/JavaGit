package mdettlaff.javagit.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

class ByteArrayBuilder {

	private ByteArrayOutputStream bytes;

	public ByteArrayBuilder() {
		bytes = new ByteArrayOutputStream();
	}

	public ByteArrayBuilder bytes(byte[] value) {
		try {
			bytes.write(value);
			return this;
		} catch (IOException e) {
			throw new IllegalStateException("Cannot write to byte stream", e);
		}
	}

	public void singleByte(int value) {
		bytes.write(value);
	}

	public ByteArrayBuilder string(String value) {
		return bytes(toByteArray(value));
	}

	public ByteArrayBuilder line(String value) {
		string(value);
		newline();
		return this;
	}

	public ByteArrayBuilder newline() {
		return string("\n");
	}

	public ByteArrayBuilder field(String name, byte[] value) {
		string(name);
		string(" ");
		bytes(value);
		newline();
		return this;
	}

	public ByteArrayBuilder field(String name, String value) {
		return field(name, toByteArray(value));
	}

	public ByteArrayBuilder field(String name, ObjectId value) {
		return field(name, value.getValue());
	}

	private byte[] toByteArray(String value) {
		return value.getBytes(Constants.ENCODING);
	}

	public byte[] build() {
		return bytes.toByteArray();
	}
}
