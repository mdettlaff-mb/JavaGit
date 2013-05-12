package mdettlaff.javagit.command.plumbing;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import mdettlaff.javagit.command.common.Arguments;
import mdettlaff.javagit.command.common.Command;
import mdettlaff.javagit.common.ObjectId;
import mdettlaff.javagit.index.Index;
import mdettlaff.javagit.index.IndexEntry;
import mdettlaff.javagit.index.IndexIO;
import mdettlaff.javagit.object.GitObject;
import mdettlaff.javagit.object.GitObject.Type;
import mdettlaff.javagit.object.GitObjects;
import mdettlaff.javagit.object.Tree;
import mdettlaff.javagit.object.Tree.Node;
import mdettlaff.javagit.object.Tree.Node.Mode;

import com.google.common.collect.ImmutableList;

public class WriteTree implements Command {

	private final IndexIO indexIO;
	private final GitObjects objects;

	private Map<Path, ObjectId> blobs;

	public WriteTree(IndexIO indexIO, GitObjects objects) {
		this.indexIO = indexIO;
		this.objects = objects;
		this.blobs = new LinkedHashMap<>();
	}

	@Override
	public void execute(Arguments args) throws IOException {
		ObjectId id = execute();
		System.out.println(id);
	}

	public ObjectId execute() throws IOException {
		Index index = indexIO.read();
		for (IndexEntry entry : index.getEntries()) {
			blobs.put(entry.getPath(), entry.getId());
		}
		return writeTreeObject(Paths.get(""), ImmutableList.copyOf(blobs.keySet()));
	}

	private ObjectId writeTreeObject(Path prefix, List<Path> paths) throws IOException {
		List<Node> nodes = new ArrayList<>();
		int i = 0;
		while (i < paths.size()) {
			if (paths.get(i).getNameCount() == 1) {
				String fileName = paths.get(i).getFileName().toString();
				Path blobKey = Paths.get(prefix.toString(), paths.get(i).toString());
				nodes.add(new Node(Mode.NORMAL, blobs.get(blobKey), fileName));
				i++;
			} else {
				Path currentDir = paths.get(i).getName(0);
				List<Path> dirSubtreePaths = new ArrayList<>();
				while (i < paths.size() && paths.get(i).getNameCount() > 1) {
					if (paths.get(i).getName(0).equals(currentDir)) {
						dirSubtreePaths.add(paths.get(i).subpath(1, paths.get(i).getNameCount()));
					} else {
						break;
					}
					i++;
				}
				Path newPrefix = Paths.get(prefix.toString(), currentDir.toString());
				ObjectId treeObjectId = writeTreeObject(newPrefix, dirSubtreePaths);
				nodes.add(new Node(Mode.DIRECTORY, treeObjectId, currentDir.toString()));
			}
		}
		Tree tree = new Tree(ImmutableList.copyOf(nodes));
		GitObject treeObject = new GitObject(Type.TREE, tree.toByteArray().length, tree);
		return objects.write(treeObject);
	}
}
