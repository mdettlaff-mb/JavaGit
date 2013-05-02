package mdettlaff.javagit.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.FilenameUtils;

public class Filesystem {

	public InputStream read(String path) throws IOException {
		return new FileInputStream(FilenameUtils.normalize(path));
	}

	public OutputStream write(String path) throws FileNotFoundException {
		return new FileOutputStream(FilenameUtils.normalize(path));
	}

	public boolean exists(String path) {
		return new File(FilenameUtils.normalize(path)).exists();
	}
}
