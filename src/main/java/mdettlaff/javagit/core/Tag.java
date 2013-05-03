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
		ByteArrayBuilder bytes = new ByteArrayBuilder();
		bytes.field("object", object);
		bytes.field("type", type.getLiteral());
		bytes.field("tag", tag);
		bytes.field("tagger", tagger.toByteArray());
		bytes.newline();
		bytes.line(message);
		return bytes.build();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("object ").append(object).append('\n');
		builder.append("type ").append(type.getLiteral()).append('\n');
		builder.append("tag ").append(tag).append('\n');
		builder.append("tagger ").append(tagger).append('\n');
		builder.append('\n');
		builder.append(message).append('\n');
		return builder.toString();
	}
}
