package mdettlaff.javagit.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import mdettlaff.javagit.core.Commit;
import mdettlaff.javagit.core.Creator;
import mdettlaff.javagit.core.GitObject;
import mdettlaff.javagit.core.GitObjects;
import mdettlaff.javagit.core.ObjectId;

import com.google.common.base.Preconditions;

public class Log implements Command {

	private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	private final RevList revList;
	private final GitObjects objects;

	public Log(RevList revList, GitObjects objects) {
		this.revList = revList;
		this.objects = objects;
	}

	@Override
	public void execute(String[] args) throws IOException {
		Preconditions.checkArgument(args.length > 0, "Object ID parameter is required");
		boolean showMerges = !(args.length > 1 && args[1].equals("--no-merges"));
		ObjectId id = new ObjectId(args[0]);
		List<ObjectId> ids = revList.execute(id, showMerges);
		execute(ids);
	}

	private void execute(List<ObjectId> ids) throws IOException {
		for (ObjectId id : ids) {
			GitObject object = objects.read(id);
			Commit commit = (Commit) object.getContent();
			String commitAsString = commitToString(commit, id);
			System.out.println(commitAsString);
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
