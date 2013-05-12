package mdettlaff.javagit.command.porcelain;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.command.plumbing.CommitTree;
import mdettlaff.javagit.command.plumbing.RevParse;
import mdettlaff.javagit.command.plumbing.WriteTree;
import mdettlaff.javagit.common.ObjectId;
import mdettlaff.javagit.reference.References;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

public class CommitCommand implements Command {

	private final CommitTree commitTree;
	private final WriteTree writeTree;
	private final RevParse revParse;
	private final References refs;

	public CommitCommand(CommitTree commitTree, WriteTree writeTree, RevParse revParse, References refs) {
		this.commitTree = commitTree;
		this.writeTree = writeTree;
		this.revParse = revParse;
		this.refs = refs;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		Preconditions.checkArgument(!args.getParameters().isEmpty(), "Message parameter is required");
		String message = StringUtils.join(args.getParameters(), ' ');
		execute(message);
	}

	private void execute(String message) throws IOException {
		ObjectId tree = writeTree.execute();
		List<ObjectId> parents = Arrays.asList(revParse.execute("HEAD"));
		ObjectId commitId = commitTree.execute(tree, message, parents);
		refs.update("HEAD", commitId);
	}
}
