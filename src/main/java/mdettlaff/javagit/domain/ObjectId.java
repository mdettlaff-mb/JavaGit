package mdettlaff.javagit.domain;

public class ObjectId {

	private final String value;

	public ObjectId(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
