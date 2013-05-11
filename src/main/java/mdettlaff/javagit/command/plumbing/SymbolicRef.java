package mdettlaff.javagit.command.plumbing;

import java.io.IOException;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.reference.References;

import com.google.common.base.Preconditions;

public class SymbolicRef implements Command {

	private final References refs;

	public SymbolicRef(References refs) {
		this.refs = refs;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		Preconditions.checkArgument(!args.getParameters().isEmpty(), "Reference name parameter is required");
		String referenceName = args.getParameters().get(0);
		if (args.getParameters().size() > 1) {
			String newValue = args.getParameters().get(1);
			refs.updateSymbolic(referenceName, newValue);
		} else {
			String value = refs.readSymbolic(referenceName);
			System.out.println(value);
		}
	}
}
