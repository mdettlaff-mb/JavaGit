package mdettlaff.javagit.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FilesWrapper {

	public InputStream newInputStream(Path path) throws IOException {
		return Files.newInputStream(path);
	}

	public OutputStream newOutputStream(Path path) throws IOException {
		return Files.newOutputStream(path);
	}

	public boolean exists(Path path) {
		return Files.exists(path);
	}
}
