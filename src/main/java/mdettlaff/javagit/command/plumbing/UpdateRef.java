package mdettlaff.javagit.command.plumbing;

import java.io.IOException;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.common.ObjectId;
import mdettlaff.javagit.reference.References;

import com.google.common.base.Preconditions;

public class UpdateRef implements Command {

	private final References refs;

	public UpdateRef(References refs) {
		this.refs = refs;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		boolean delete = args.isOptionSet("d");
		if (delete) {
			Preconditions.checkArgument(!args.getParameters().isEmpty(), "Reference name parameter is required");
			String referenceName = args.getParameters().get(0);
			refs.delete(referenceName);
		} else {
			Preconditions.checkArgument(args.getParameters().size() > 1, "Two parameters are required");
			String referenceName = args.getParameters().get(0);
			ObjectId newValue = new ObjectId(args.getParameters().get(1));
			refs.update(referenceName, newValue);
		}
	}
}
