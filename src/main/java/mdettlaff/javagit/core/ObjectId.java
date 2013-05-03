package mdettlaff.javagit.core;

import java.util.Objects;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class ObjectId {

	private final String value;

	public ObjectId(String value) {
		this.value = value;
	}

	public ObjectId(byte[] value) {
		this(Hex.encodeHexString(value));
	}

	public String getValue() {
		return value;
	}

	public byte[] toByteArray() {
		try {
			return Hex.decodeHex(value.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Invalid hex string", e);
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ObjectId other = (ObjectId) obj;
		return Objects.equals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public String toString() {
		return value;
	}
}
