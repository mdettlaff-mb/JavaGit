package mdettlaff.javagit.common;

import java.util.Arrays;
import java.util.Objects;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.google.common.base.Preconditions;

public class ObjectId {

	private final byte[] value;

	public ObjectId(String value) {
		Preconditions.checkArgument(value.length() == 40, "Invalid object ID: " + value);
		try {
			this.value = Hex.decodeHex(value.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalArgumentException("Invalid object ID: " + value, e);
		}
	}

	public ObjectId(byte[] value) {
		this.value = value;
	}

	public String getValue() {
		return Hex.encodeHexString(value);
	}

	public byte[] toByteArray() {
		return value;
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
		return Objects.deepEquals(value, other.value);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(value);
	}

	@Override
	public String toString() {
		return getValue();
	}
}
