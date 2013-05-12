package mdettlaff.javagit.command;

import java.nio.file.Path;
import java.nio.file.Paths;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.command.plumbing.CatFile;
import mdettlaff.javagit.command.plumbing.CommitTree;
import mdettlaff.javagit.command.plumbing.HashObject;
import mdettlaff.javagit.command.plumbing.LsFiles;
import mdettlaff.javagit.command.plumbing.RevList;
import mdettlaff.javagit.command.plumbing.RevParse;
import mdettlaff.javagit.command.plumbing.SymbolicRef;
import mdettlaff.javagit.command.plumbing.UpdateRef;
import mdettlaff.javagit.command.porcelain.Log;
import mdettlaff.javagit.common.FilesWrapper;
import mdettlaff.javagit.config.Config;
import mdettlaff.javagit.index.IndexIO;
import mdettlaff.javagit.object.GitObjects;
import mdettlaff.javagit.reference.References;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;

public class Git implements Command {

	public static void main(String[] args) {
		try {
			new Git().execute(new Arguments(args));
		} catch (Exception e) {
			handleExceptions(e);
			System.exit(1);
		}
	}

	private static void handleExceptions(Exception e) {
		if (StringUtils.isNotBlank(e.getMessage())) {
			System.err.println("error: " + e.getMessage());
		} else {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Arguments args) throws Exception {
		Preconditions.checkArgument(!args.getParameters().isEmpty(), "No command specified");
		String commandArgument = args.getParameters().get(0);
		Command command = createCommand(commandArgument);
		args.getParameters().remove(0);
		command.execute(args);
	}

	private Command createCommand(String commandArgument) {
		FilesWrapper files = new FilesWrapper();
		GitObjects objects = new GitObjects(files);
		References refs = new References(files);
		Path configPath = Paths.get(System.getProperty("user.home"), ".gitconfig");
		Config config = new Config(files, configPath);
		switch (commandArgument) {
		case "cat-file":
			return new CatFile(objects);
		case "hash-object":
			return new HashObject(objects);
		case "commit-tree":
			return new CommitTree(objects, config);
		case "rev-list":
			return new RevList(objects);
		case "rev-parse":
			return new RevParse(refs, objects);
		case "update-ref":
			return new UpdateRef(refs);
		case "symbolic-ref":
			return new SymbolicRef(refs);
		case "ls-files":
			return new LsFiles(new IndexIO(files));
		case "log":
			return new Log(new RevList(objects), new RevParse(refs, objects), objects);
		default:
			throw new IllegalArgumentException("Unknown command: " + commandArgument);
		}
	}
}
