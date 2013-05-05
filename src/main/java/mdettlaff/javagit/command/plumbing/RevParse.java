package mdettlaff.javagit.command.plumbing;

import java.io.IOException;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.common.ObjectId;
import mdettlaff.javagit.reference.References;

import com.google.common.base.Preconditions;

public class RevParse implements Command {

	private final References refs;

	public RevParse(References refs) {
		this.refs = refs;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		Preconditions.checkArgument(!args.getParameters().isEmpty(), "Revision parameter is required");
		String revision = args.getParameters().get(0);
		ObjectId id = execute(revision);
		System.out.println(id);
	}

	public ObjectId execute(String revision) throws IOException {
		try {
			return new ObjectId(revision);
		} catch (IllegalArgumentException e) {
			return refs.resolve(revision);
		}
	}
}
