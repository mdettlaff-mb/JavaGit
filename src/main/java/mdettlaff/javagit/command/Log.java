package mdettlaff.javagit.command;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import mdettlaff.javagit.object.Commit;
import mdettlaff.javagit.object.Creator;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObjects;
import mdettlaff.javagit.object.ObjectId;

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
	public void execute(Arguments args) throws IOException {
		Preconditions.checkArgument(!args.getParameters().isEmpty(), "Object ID parameter is required");
		ObjectId id = new ObjectId(args.getParameters().get(0));
		boolean showMerges = args.isOptionSet("no-merges");
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
