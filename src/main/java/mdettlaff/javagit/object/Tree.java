package mdettlaff.javagit.object;

import java.util.List;

import mdettlaff.javagit.common.FileMode;
import mdettlaff.javagit.common.ObjectId;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableList;

public class Tree implements ObjectContent {

	private final List<Node> nodes;

	public Tree(ImmutableList<Node> nodes) {
		this.nodes = nodes;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	@Override
	public byte[] toByteArray() {
		ByteArrayBuilder bytes = new ByteArrayBuilder();
		for (Node node : nodes) {
			bytes.bytes(node.toByteArray());
		}
		return bytes.build();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Node node : nodes) {
			builder.append(node).append('\n');
		}
		return builder.toString();
	}

	public static class Node {

		private final FileMode mode;
		private final ObjectId value;
		private final String name;

		public Node(FileMode mode, ObjectId value, String name) {
			this.mode = mode;
			this.value = value;
			this.name = name;
		}

		public FileMode getMode() {
			return mode;
		}

		public ObjectId getValue() {
			return value;
		}

		public String getName() {
			return name;
		}

		public byte[] toByteArray() {
			ByteArrayBuilder bytes = new ByteArrayBuilder();
			bytes.string(mode.getLiteral());
			bytes.string(" ");
			bytes.string(name);
			bytes.singleByte(0);
			bytes.bytes(value.toByteArray());
			return bytes.build();
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(StringUtils.leftPad(mode.getLiteral(), 6, '0'));
			builder.append(' ');
			builder.append(value);
			builder.append(' ');
			builder.append(name);
			return builder.toString();
		}
	}
}
