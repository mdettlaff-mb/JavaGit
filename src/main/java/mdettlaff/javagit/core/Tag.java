package mdettlaff.javagit.core;

import mdettlaff.javagit.core.GitObject.Type;

public class Tag implements ObjectContent {

	private final ObjectId object;
	private final Type type;
	private final String tag;
	private final Creator tagger;
	private final String message;

	public Tag(ObjectId object, Type type, String tag, Creator tagger, String message) {
		this.object = object;
		this.type = type;
		this.tag = tag;
		this.tagger = tagger;
		this.message = message;
	}

	public ObjectId getObject() {
		return object;
	}

	public Type getType() {
		return type;
	}

	public String getTag() {
		return tag;
	}

	public Creator getTagger() {
		return tagger;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public byte[] toByteArray() {
		StringBuilder content = new StringBuilder();
		content.append("object " + object + "\n");
		content.append("type " + type.getLiteral() + "\n");
		content.append("tag " + tag + "\n");
		content.append("tagger ");
		content.append(new String(tagger.toByteArray()) + "\n");
		content.append('\n');
		content.append(message + "\n");
		return content.toString().getBytes();
	}

	@Override
	public String toString() {
		return new String(toByteArray());
	}
}
