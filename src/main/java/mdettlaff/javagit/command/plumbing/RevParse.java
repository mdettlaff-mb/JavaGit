package mdettlaff.javagit.command.plumbing;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

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
			List<String> prefixes = Arrays.asList("", "refs/", "refs/tags/", "refs/heads/");
			return parseReference(revision, prefixes.iterator());
		}
	}

	private ObjectId parseReference(String referenceName, Iterator<String> prefixes) throws IOException {
		Preconditions.checkArgument(prefixes.hasNext(), "Unknown revision: " + referenceName);
		try {
			return refs.resolve(prefixes.next() + referenceName);
		} catch (IllegalArgumentException e) {
			return parseReference(referenceName, prefixes);
		}
	}
}
