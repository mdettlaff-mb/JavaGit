package mdettlaff.javagit.command.plumbing;

import java.io.IOException;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.index.Index;
import mdettlaff.javagit.index.IndexIO;

public class LsFiles implements Command {

	private final IndexIO indexIO;

	public LsFiles(IndexIO indexIO) {
		this.indexIO = indexIO;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		boolean stage = args.isOptionSet("s");
		Index index = indexIO.read();
		if (stage) {
			System.out.print(index);
		} else {
			System.out.print(getPaths(index));
		}
	}

	private String getPaths(Index index) {
		StringBuilder builder = new StringBuilder();
		for (Index.Entry entry : index.getEntries()) {
			builder.append(entry.getPath()).append('\n');
		}
		return builder.toString();
	}
}
