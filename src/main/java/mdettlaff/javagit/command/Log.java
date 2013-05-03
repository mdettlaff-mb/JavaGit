package mdettlaff.javagit.command;

import java.io.IOException;
import java.text.SimpleDateFormat;

import com.google.common.base.Preconditions;

import mdettlaff.javagit.core.Commit;
import mdettlaff.javagit.core.Creator;
import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObject.Type;
import mdettlaff.javagit.core.GitObjects;
import mdettlaff.javagit.core.ObjectId;

public class Log implements Command {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private final GitObjects objects;

	public Log(GitObjects objects) {
		this.objects = objects;
	}

	@Override
	public void execute(String[] args) throws IOException {
		Preconditions.checkArgument(args.length > 0, "Object ID parameter is required");
		log(new ObjectId(args[0]));
	}

	private void log(ObjectId id) throws IOException {
		GitObject object = objects.read(id);
		Preconditions.checkArgument(object.getType() == Type.COMMIT, "Object with given ID must be a commit");
		Commit commit = (Commit) object.getContent();
		String commitAsString = commitToString(commit, id);
		System.out.println(commitAsString);
		if (!commit.getParents().isEmpty()) {
			ObjectId parentId = commit.getParents().get(0);
			log(parentId);
		}
	}

	private String commitToString(Commit commit, ObjectId id) {
		StringBuilder builder = new StringBuilder();
		builder.append("commit ").append(id).append('\n');
		Creator author = commit.getAuthor();
		builder.append("Author:\t").append(author.getName()).append(' ');
		builder.append('<').append(author.getEmail()).append('>');
		builder.append('\n');
		String date = new SimpleDateFormat(DATE_FORMAT).format(author.getDate().toDate());
		builder.append("Date:\t").append(date).append(' ');
		builder.append(author.getTimezone()).append('\n');
		builder.append('\n');
		builder.append(commit.getMessage()).append('\n');
		return builder.toString();
	}
}
