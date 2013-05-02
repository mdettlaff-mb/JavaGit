package mdettlaff.javagit.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;

public class Filesystem {

	public InputStream readFile(String path) throws IOException {
		return new FileInputStream(FilenameUtils.normalize(path));
	}
}
