package mdettlaff.javagit.command.plumbing;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.common.FileMode;
import mdettlaff.javagit.common.ObjectId;
import mdettlaff.javagit.index.Index;
import mdettlaff.javagit.index.IndexIO;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObject.Type;
import mdettlaff.javagit.object.GitObjects;
import mdettlaff.javagit.object.Tree;
import mdettlaff.javagit.object.Tree.Node;

import com.google.common.collect.ImmutableList;

public class WriteTree implements Command {

	private final IndexIO indexIO;
	private final GitObjects objects;

	public WriteTree(IndexIO indexIO, GitObjects objects) {
		this.indexIO = indexIO;
		this.objects = objects;
	}

	@Override
	public void execute(Arguments args) throws IOException {
		ObjectId id = execute();
		System.out.println(id);
	}

	public ObjectId execute() throws IOException {
		Index index = indexIO.read();
		return writeTreeObject(Paths.get(""), index.getEntries());
	}

	private ObjectId writeTreeObject(Path prefix, List<Index.Entry> entries) throws IOException {
		List<Node> nodes = new ArrayList<>();
		int i = 0;
		while (i < entries.size()) {
			Path path = entries.get(i).getPath();
			if (path.getNameCount() == 1) {
				String fileName = path.getFileName().toString();
				nodes.add(new Node(entries.get(i).getMode(), entries.get(i).getId(), fileName));
				i++;
			} else {
				Path directory = path.getName(0);
				List<Index.Entry> subtrees = new ArrayList<>();
				while (i < entries.size() && path.getNameCount() > 1) {
					path = entries.get(i).getPath();
					if (path.getName(0).equals(directory)) {
						Path subpath = path.subpath(1, path.getNameCount());
						Index.Entry entry = entries.get(i);
						subtrees.add(new Index.Entry(subpath, entry.getId(), entry.getMode()));
					} else {
						break;
					}
					i++;
				}
				Path newPrefix = Paths.get(prefix.toString(), directory.toString());
				ObjectId treeObjectId = writeTreeObject(newPrefix, subtrees);
				nodes.add(new Node(FileMode.DIRECTORY, treeObjectId, directory.toString()));
			}
		}
		Tree tree = new Tree(ImmutableList.copyOf(nodes));
		GitObject treeObject = new GitObject(Type.TREE, tree.toByteArray().length, tree);
		return objects.write(treeObject);
	}
}
