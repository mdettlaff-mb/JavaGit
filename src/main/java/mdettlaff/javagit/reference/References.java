package mdettlaff.javagit.reference;

import java.io.IOException;
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
			return resolve(getSymbolic(referenceValue));
		} else {
			return new ObjectId(referenceValue);
		}
	}

	public void delete(String name) throws IOException {
		Preconditions.checkArgument(files.exists(toPath(name)), "Unknown reference: " + name);
		files.delete(toPath(name));
	}

	public void update(String name, ObjectId newValue) throws IOException {
		if (files.exists(toPath(name))) {
			String referenceValue = readFileContent(name);
			if (isSymbolic(referenceValue)) {
				update(getSymbolic(referenceValue), newValue);
				return;
			}
		}
		files.write(toPath(name), newValue.getValue() + '\n');
	}

	public String readSymbolic(String name) throws IOException {
		Preconditions.checkArgument(files.exists(toPath(name)), "Unknown symbolic ref: " + name);
		String referenceValue = readFileContent(name);
		Preconditions.checkArgument(isSymbolic(referenceValue), "Not a symbolic ref: " + name);
		return getSymbolic(referenceValue);
	}

	public void updateSymbolic(String name, String newValue) throws IOException {
		files.write(toPath(name), SYMBOLIC_REFERENCE_PREFIX + newValue + '\n');
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

	private String getSymbolic(String referenceValue) {
		return referenceValue.substring(SYMBOLIC_REFERENCE_PREFIX.length());
	}
}
