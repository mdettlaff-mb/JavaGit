package mdettlaff.javagit.reference;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import mdettlaff.javagit.common.Constants;
import mdettlaff.javagit.common.FilesWrapper;
import mdettlaff.javagit.common.ObjectId;

import com.google.common.base.Preconditions;

public class References {

	private static final String SYMBOLIC_REFERENCE_PREFIX = "ref: ";

	private final FilesWrapper files;

	public References(FilesWrapper files) {
		this.files = files;
	}

	public ObjectId resolve(String name) throws IOException {
		Preconditions.checkArgument(files.exists(toPath(name)), "Unknown reference: " + name);
		String referenceValue = readFileContent(name);
		if (isSymbolic(referenceValue)) {
			return read(readSymbolicValue(referenceValue));
		} else {
			return readValue(referenceValue);
		}
	}

	private ObjectId read(String name) throws IOException {
		Preconditions.checkArgument(files.exists(toPath(name)), "Invalid reference: " + name);
		return readValue(readFileContent(name));
	}

	private ObjectId readValue(String referenceValue) {
		return new ObjectId(referenceValue);
	}

	public void delete(String name) throws IOException {
		Preconditions.checkArgument(files.exists(toPath(name)), "Unknown reference: " + name);
		files.delete(toPath(name));
	}

	public void update(String name, ObjectId newValue) throws IOException {
		Path path = toPath(name);
		if (Files.exists(path)) {
			String referenceValue = readFileContent(name);
			if (isSymbolic(referenceValue)) {
				path = toPath(readSymbolicValue(referenceValue));
			}
		}
		files.write(path, newValue.getValue());
	}

	public String readSymbolic(String name) throws IOException {
		Preconditions.checkArgument(files.exists(toPath(name)), "Unknown symbolic ref: " + name);
		String referenceValue = readFileContent(name);
		Preconditions.checkArgument(isSymbolic(referenceValue), "Not a symbolic ref: " + name);
		return readSymbolicValue(referenceValue);
	}

	private String readFileContent(String referenceName) throws IOException {
		return files.readAllChars(toPath(referenceName)).trim();
	}

	private Path toPath(String referenceName) {
		return Paths.get(Constants.GIT_DIR, referenceName);
	}

	private boolean isSymbolic(String referenceValue) {
		return referenceValue.startsWith(SYMBOLIC_REFERENCE_PREFIX);
	}

	private String readSymbolicValue(String referenceValue) {
		return referenceValue.substring(SYMBOLIC_REFERENCE_PREFIX.length());
	}

	public void updateSymbolic(String name, String newValue) throws IOException {
		files.write(toPath(name), SYMBOLIC_REFERENCE_PREFIX + newValue);
	}
}
