package mdettlaff.javagit.common;

import java.io.IOException;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.Path;

public class FilenamePrefixFilter implements Filter<Path> {

	private final String prefix;

	public FilenamePrefixFilter(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public boolean accept(Path path) throws IOException {
		return path.getFileName().toString().startsWith(prefix);
	}
}
