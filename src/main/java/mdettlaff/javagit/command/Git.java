package mdettlaff.javagit.command;

import java.nio.file.Path;
import java.nio.file.Paths;

import mdettlaff.javagit.core.Config;
import mdettlaff.javagit.core.FilesWrapper;
import mdettlaff.javagit.core.GitObjects;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

public class Git implements Command {

	public static void main(String[] args) throws Exception {
		new Git().execute(args);
	}

	@Override
	public void execute(String[] args) throws Exception {
		try {
			Preconditions.checkArgument(args.length > 0, "No command specified");
			String commandArgument = args[0];
			Command command = createCommand(commandArgument);
			command.execute(ArrayUtils.remove(args, 0));
		} catch (Exception e) {
			handleExceptions(e);
		}
	}

	private Command createCommand(String commandArgument) {
		FilesWrapper files = new FilesWrapper();
		GitObjects objects = new GitObjects(files);
		Path configPath = Paths.get(System.getProperty("user.home"), ".gitconfig");
		Config config = new Config(files, configPath);
		switch (commandArgument) {
		case "cat-file":
			return new CatFile(objects);
		case "hash-object":
			return new HashObject(objects);
		case "log":
			return new Log(objects);
		case "commit-tree":
			return new CommitTree(objects, config);
		default:
			throw new IllegalArgumentException("Unknown command: " + commandArgument);
		}
	}

	private void handleExceptions(Exception e) throws Exception {
		if (StringUtils.isNotBlank(e.getMessage())) {
			System.err.println("error: " + e.getMessage());
		} else {
			throw e;
		}
	}
}
