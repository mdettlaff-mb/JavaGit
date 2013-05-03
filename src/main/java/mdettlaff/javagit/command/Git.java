package mdettlaff.javagit.command;

import org.apache.commons.lang3.ArrayUtils;

import mdettlaff.javagit.core.Filesystem;
import mdettlaff.javagit.core.GitObjects;

public class Git implements Command {

	public static void main(String[] args) throws Exception {
		new Git().execute(args);
	}

	@Override
	public void execute(String[] args) throws Exception {
		if (args.length == 0) {
			throw new IllegalArgumentException("No command specified");
		}
		String commandArgument = args[0];
		Command command = createCommand(commandArgument);
		command.execute(ArrayUtils.remove(args, 0));
	}

	private Command createCommand(String commandArgument) {
		GitObjects objects = new GitObjects(new Filesystem());
		switch (commandArgument) {
		case "cat-file":
			return new CatFile(objects);
		case "hash-object":
			return new HashObject(objects);
		case "log":
			return new Log(objects);
		default:
			throw new IllegalArgumentException("Unknown command: " + commandArgument);
		}
	}
}
