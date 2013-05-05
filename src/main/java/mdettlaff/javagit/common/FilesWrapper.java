package mdettlaff.javagit.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

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

	public List<String> readAllLines(Path path) throws IOException {
		return Files.readAllLines(path, Charset.defaultCharset());
	}

	public String readAllChars(Path path) throws IOException {
		return new String(Files.readAllBytes(path));
	}

	public void write(Path path, String content) throws IOException {
		Files.write(path, content.getBytes());
	}

	public void delete(Path path) throws IOException {
		Files.delete(path);
	}
}
