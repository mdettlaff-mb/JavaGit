package mdettlaff.javagit.core;

import java.util.List;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
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
		byte[] bytes = new byte[0];
		for (Node node : nodes) {
			bytes = ArrayUtils.addAll(bytes, node.toByteArray());
		}
		return bytes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Node node : nodes) {
			builder.append(node + "\n");
		}
		return builder.toString();
	}

	public static class Node {

		private final Mode mode;
		private final ObjectId value;
		private final String path;

		public Node(Mode mode, ObjectId value, String path) {
			this.mode = mode;
			this.value = value;
			this.path = path;
		}

		public Mode getMode() {
			return mode;
		}

		public ObjectId getValue() {
			return value;
		}

		public String getPath() {
			return path;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append(StringUtils.leftPad(mode.getLiteral(), 6, '0'));
			builder.append(' ');
			builder.append(value.getValue());
			builder.append(' ');
			builder.append(path);
			return builder.toString();
		}

		public byte[] toByteArray() {
			byte[] bytes = mode.getLiteral().getBytes();
			bytes = ArrayUtils.add(bytes, (byte) ' ');
			bytes = ArrayUtils.addAll(bytes, path.getBytes());
			bytes = ArrayUtils.add(bytes, (byte) 0);
			try {
				return ArrayUtils.addAll(bytes, Hex.decodeHex(value.getValue().toCharArray()));
			} catch (DecoderException e) {
				throw new IllegalStateException("Invalid hex string", e);
			}
		}

		public static enum Mode {

			NORMAL("100644"),
			EXECUTABLE("100755"),
			SYMLINK("120000"),
			DIRECTORY("40000");

			private String literal;

			private Mode(String literal) {
				this.literal = literal;
			}

			public String getLiteral() {
				return literal;
			}

			public static Mode getByLiteral(String literal) {
				for (Mode mode : values()) {
					if (mode.literal.equals(literal)) {
						return mode;
					}
				}
				throw new IllegalArgumentException("Unknown mode literal: " + literal);
			}
		}
	}
}
